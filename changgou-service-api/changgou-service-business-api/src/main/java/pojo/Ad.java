package pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name="tb_ad")
public class Ad implements Serializable {

	@Id
	private Integer id;//ID
	
	private String name;//广告名称
	private String position;//广告位置
	private java.util.Date startTime;//开始时间
	private java.util.Date endTime;//到期时间
	private String status;//状态
	private String image;//图片地址
	private String url;//URL
	private String remarks;//备注
}
