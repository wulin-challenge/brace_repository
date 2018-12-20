package cn.wulin.brace.zookeeper.test.listener.lock2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

/**
 * 模拟数据库
 * @author wubo
 *
 */
public class ListenerCommonDB {
	
	private static final List<User> DB_USER = new ArrayList<User>();
	
	/**
	 * 查找所有
	 * @return
	 */
	public static List<User> findAllUser(){
		return DB_USER;
	}
	
	public static User findByIdUser(String id){
		List<User> findAllUser = findAllUser();
		if(findAllUser == null || findAllUser.size() ==0){
			return null;
		}
		for (User user : findAllUser) {
			if(user.getId().equals(id)){
				return user;
			}
		}
		return null;
	}
	
	public static void saveUser(User user){
		synchronized (ListenerCommonDB.class) {
			DB_USER.add(user);
		}
	}
	
	public static void updateUser(User user){
		synchronized (ListenerCommonDB.class) {
			User oldUser = findByIdUser(user.getId());
			BeanUtils.copyProperties(user, oldUser);
		}
	}
	
	public static class User{
		public User() {}
		public User(String id, String username, String password, Date createDate) {
			super();
			this.id = id;
			this.username = username;
			this.password = password;
			this.createDate = createDate;
		}

		private String id;
		private String username;
		private String password;
		private Date createDate;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public Date getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
		@Override
		public String toString() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuilder sb = new StringBuilder();
			sb.append("\n{id:"+id);
			sb.append(",username:"+username);
			sb.append(",password:"+password);
			String date = createDate == null?"":sdf.format(createDate);
			sb.append(",createDate:"+date+"}");
			return sb.toString();
		}
	}
	
//	public static void main(String[] args) {
//		User user1 = new User("1", "wulin_1", "123", new Date());
//		User user2 = new User("2", "wulin_2", "456", new Date());
//		User user3 = new User("3", "wulin_3", "789", null);
//		saveUser(user1);
//		saveUser(user2);
//		saveUser(user3);
//		List<User> findAllUser = findAllUser();
//		System.out.println(findAllUser);
//	}
}
