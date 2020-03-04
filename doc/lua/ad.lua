ngx.header.content_type="application/json;charset=utf8"

local uri_args = ngx.req.get_uri_args()
local position = uri_args["position"]

--获取本地缓存
local cache_ngx = ngx.shared.dis_cache;
--根据ID 获取本地缓存数据
local adCache = cache_ngx:get('ad_cache_'..position);

if adCache == "" or adCache == nil then

	--引入redis库
	local redis = require("resty.redis");
	--创建redis对象
	local red = redis:new()
	--设置超时时间
	red:set_timeout(2000)
	--连接
	local ok, err = red:connect("127.0.0.1", 6379)
	--获取key的值
	local rescontent=red:get("ad_"..position)
	
	if ngx.null == rescontent then
		local cjson = require("cjson")
		local mysql = require("resty.mysql")
		local db = mysql:new()
		db:set_timeout(1000)  
		local props = {  
			host = "127.0.0.1",  
			port = 3306,  
			database = "changgou_business",  
			user = "root",  
			password = "admin"  
		}
		
		local res = db:connect(props)  
		local select_sql = "select * from tb_ad where status ='1' and position='"..position.."'"
		res = db:query(select_sql)  
		local responsejson = cjson.encode(res)
		red:set("ad_"..position, responsejson)
		ngx.say("mysql输出")
		ngx.say(responsejson)
		db:close()
	else
		--将redis中获取到的数据存入nginx本地缓存
		cache_ngx:set('ad_cache_'..position, rescontent, 1*60);
		red:close()
		--输出到返回响应中
		ngx.say("redis输出")
		ngx.say(rescontent)
	
	end
	--关闭连接
	red:close()
else
 	--nginx本地缓存中获取到数据直接输出
	ngx.say("nginx本地缓存输出")
 	ngx.say(adCache)
end



  



ngx.say("{flag:true}")