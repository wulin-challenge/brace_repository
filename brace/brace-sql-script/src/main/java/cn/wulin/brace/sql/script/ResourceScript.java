package cn.wulin.brace.sql.script;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.hutool.core.lang.UUID;
import cn.wulin.brace.sql.script.base.ScriptCreateTable;
import cn.wulin.brace.sql.script.dao.SqlScriptDao;
import cn.wulin.brace.sql.script.domain.Command;
import cn.wulin.brace.sql.script.domain.CommandReturnEnum;
import cn.wulin.brace.sql.script.domain.CommandTypeEnum;
import cn.wulin.brace.sql.script.domain.EngineParam;
import cn.wulin.brace.sql.script.domain.Sql;
import cn.wulin.brace.sql.script.domain.SqlScriptEntity;
import cn.wulin.brace.sql.script.properties.SqlScriptProperties;
import cn.wulin.brace.sql.script.utils.DatabaseUtil;
import cn.wulin.brace.sql.script.utils.JdbcUtils;
import cn.wulin.brace.sql.script.utils.ScriptUtils;
import cn.wulin.brace.sql.script.utils.ScriptUtils.SingleSql;

@SuppressWarnings("unchecked")
public class ResourceScript implements InitializingBean{
	
	/**
	 * 表示执行所有脚本
	 */
	private final static String ALL_SCRIPT = "script:all";
	private final static Logger LOGGER = LoggerFactory.getLogger(ResourceScript.class);
	
	
	private static DefaultListableBeanFactory beanFactory2;
	@Autowired
	private DefaultListableBeanFactory beanFactory;
	
	@Autowired
	private SqlScriptProperties sqlScriptProperties;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SqlScriptDao sqlScriptDao;
	private FreemarkerEngine engine = new FreemarkerEngine();
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ResourceScript.beanFactory2 = beanFactory;
		createInternalTable();
		
	}
	
	public static DefaultListableBeanFactory getBeanFactory2() {
		return beanFactory2;
	}

	public static void setBeanFactory2(DefaultListableBeanFactory beanFactory2) {
		ResourceScript.beanFactory2 = beanFactory2;
	}
	
	/**
	 * 执行脚本
	 * @param saveScript 是否保存脚本到数据库
	 * @throws Exception
	 */
	public void executeScript(Boolean saveScript) throws Exception {
		executeScript(new HashMap<>(),ALL_SCRIPT,saveScript);
	}
	
	/**
	 * 执行脚本
	 * @param params 参数
	 * @param saveScript 是否保存脚本到数据库
	 * @throws Exception
	 */
	public void executeScript(Map<String,Object> params,Boolean saveScript) throws Exception {
		executeScript(params,ALL_SCRIPT,saveScript);
	}

	/**
	 * 执行脚本
	 * @param params 参数
	 * @param name 执行命令的名称
	 * @param saveScript 是否保存脚本到数据库
	 * @throws Exception
	 */
	public void executeScript(Map<String,Object> params,String name,Boolean saveScript) throws Exception {
		executeScript(params,name, EngineParam.class,saveScript);
	}

	/**
	 * 执行脚本
	 * @param params 参数
	 * @param name 执行命令的名称
	 * @param engineParamClass freemarker引擎参数Class
	 * @param saveScript 是否保存脚本到数据库
	 * @throws Exception
	 */
	public void executeScript(Map<String,Object> params,String name, Class<? extends EngineParam> engineParamClass,Boolean saveScript) throws Exception {
		List<Resource> resources = loadScriptResource();
		
		List<Sql> sqls = loadConfigFile(resources);
		
		for (Sql sql : sqls) {
			List<Command> commandList = sql.getCommandList();
			for (Command command : commandList) {
				if(command.getType().equalsIgnoreCase(CommandTypeEnum.SELECT.getName())) {
					continue;
				}
				
				if(ALL_SCRIPT.equals(name)) {
					executeReallyScript(params, name, engineParamClass, sql, command,saveScript);
				}else {
					if(!command.getName().equals(name)) {
						continue;
					}
					executeReallyScript(params, name, engineParamClass, sql, command,saveScript);
				}
			}
		}
	}
	
	/**
	 * 得到指定名称的脚本
	 * @param params
	 * @param name
	 * @param engineParamClass
	 * @return
	 * @throws Exception
	 */
	public String getScript(String name) throws Exception{
		return getScript(new HashMap<>(), name);
	}
	
	/**
	 * 得到指定名称的脚本
	 * @param params
	 * @param name
	 * @param engineParamClass
	 * @return
	 * @throws Exception
	 */
	public String getScript(Map<String,Object> params,String name) throws Exception{
		return getScript(params, name, EngineParam.class);
	}
	
	/**
	 * 得到指定名称的脚本
	 * @param params
	 * @param name
	 * @param engineParamClass
	 * @return
	 * @throws Exception
	 */
	public String getScript(Map<String,Object> params,String name, Class<? extends EngineParam> engineParamClass) throws Exception{
		List<Resource> resources = loadScriptResource();
		List<Sql> sqls = loadConfigFile(resources);
		
		for (Sql sql : sqls) {
			List<Command> commandList = sql.getCommandList();
			for (Command command : commandList) {
				
				if(command.getName().equals(name)) {
					EngineParam engineParam = engineParamClass.newInstance();
					engineParam.setName(command.getName());
					engineParam.setCurrentScripts(sql);
					engineParam.getParams().putAll(params);
					
					String parseScript = engine.parseScript(engineParam);
					return parseScript;
				}
			}
		}
		return null;
	}
	
	private void executeReallyScript(Map<String,Object> params,String name, Class<? extends EngineParam> engineParamClass,Sql sql,Command command,Boolean saveScript) throws Exception {
		
		Map<String,Object> sqlParam = new HashMap<>();
		sqlParam.put("name", command.getName());
		SqlScriptEntity sqlScript2 = sqlScriptDao.findOneByCondition(sqlParam);
		
		if(sqlScript2 != null) {
			return;
		}
		
		EngineParam engineParam = engineParamClass.newInstance();
		engineParam.setName(command.getName());
		engineParam.setCurrentScripts(sql);
		engineParam.getParams().putAll(params);
		
		String parseScript = engine.parseScript(engineParam);
		
		if(StringUtils.isBlank(parseScript)) {
			throw new RuntimeException("freemarker 执行失败!");
		}
		
		try (Connection connection = jdbcTemplate.getDataSource().getConnection();){
			ScriptUtils.executeSqlScript(connection, parseScript,new String[] {"--","#"},new SingleSql() {
				@Override
				public String sql(String execSql) {
					execSql = execSql.replaceAll("\\%\\{", "\\$\\{");
					
					engineParam.getCurrentCommand().setText(execSql);
					execSql = engine.parseScript(engineParam);
					return execSql;
				}
			});
		} catch (Exception e) {
			LOGGER.error("执行脚本失败!",e);
		}
		
		if(saveScript) {
			saveSqlScript(command);
		}
		
	}
	
	private void saveSqlScript(Command command) throws Exception {
		SqlScriptEntity sqlScript = new SqlScriptEntity();
		long currentTime = System.currentTimeMillis();
		sqlScript.setId(UUID.randomUUID().toString());
		sqlScript.setContent(command.getText());
		sqlScript.setDatabaseType(command.getDatabase());
		sqlScript.setDescription(null);
		sqlScript.setInited(SqlScriptEntity.INITED);
		sqlScript.setName(command.getName());
		sqlScript.setReturnType(command.getReturnType());
		sqlScript.setTableName(command.getTable());
		sqlScript.setType(command.getType());
		sqlScript.setCreateDate(currentTime);
		sqlScript.setModifyDate(currentTime);
		
		sqlScriptDao.saveOrUpdateEntity(sqlScript);
	}
	
	/**
	 * 加载配置文件
	 */
	private List<Sql> loadConfigFile(List<Resource> resources){
		
		List<Sql> sqls = new ArrayList<>();
		
		for (Resource resource : resources) {
			Sql sql = new Sql();
			try {
				SAXReader reader = new SAXReader();
				Document document = reader.read(resource.getInputStream());
				Element root = document.getRootElement();
				
				//封装属性
				Attribute database = root.attribute("database");
				sql.setDatabase(database.getText());
				
				//封装子节点
				parseSqlElement(root, sql);
				
				sqls.add(sql);
			} catch (Exception e) {
				LOGGER.error("sql_command 配置文件加载失败!",e);
			}
		}
		return sqls;
	}
	
	/**
	 * 解析command元素
	 * @param parent 父亲元素
	 * @param sql 
	 */
	private void parseSqlElement(Element parent,Sql sql){
		List<Element> elementList = parent.elements("command");
		for (Element element : elementList) {
			Command command = new Command();
			
			//封装属性
			Attribute type = element.attribute("type");
			Attribute name = element.attribute("name");
			Attribute table = element.attribute("table");
			Attribute returnType = element.attribute("returnType");
			
			command.setType(type.getText());
			command.setName(name.getText());
			command.setTable(table == null?"":table.getText());
			command.setReturnType(returnType == null?CommandReturnEnum.VOID.getName():returnType.getText());
			command.setDatabase(sql.getDatabase());
			
			//封装元素内容
			command.setText(element.getText());
			
			sql.getCommandList().add(command);
		}
	}
	
	/**
	 * 创建内部使用的表
	 */
	private void createInternalTable() {
		String dataBaseType = DatabaseUtil.getDataBaseType(jdbcTemplate);
		
		Map<String, ScriptCreateTable> beansOfType = beanFactory.getBeansOfType(ScriptCreateTable.class);
		Collection<ScriptCreateTable> values = beansOfType.values();
		
		ScriptCreateTable realtimeCreateTable = null;
		
		for (ScriptCreateTable realtimeCreateTable2 : values) {
			if(realtimeCreateTable2.support(dataBaseType)) {
				realtimeCreateTable = realtimeCreateTable2;
			}
		}
		
		if(realtimeCreateTable == null) {
			LOGGER.error("该数据库类型不支持! 数据库类型 "+dataBaseType+",请实现 cn.wulin.brace.sql.script.base.ScriptCreateTable");
			System.exit(1);
		}
		
		//数据源配置
		String tableSql = realtimeCreateTable.dataSourceTable();
		createInternalTable("sql_script", tableSql); //
	}
	
	private void createInternalTable(String table,String tableSql) {
		try (Connection connection = jdbcTemplate.getDataSource().getConnection();){
			boolean exist = true;
			
			/*
			 * 处理表名为大小写问题
			 */
			//判断表是否存在
			ResultSet tables = connection.getMetaData().getTables(connection.getCatalog(), null, table, null);
			if(tables ==null || !tables.next()) {
				exist = false;
			}
			
			// 如果表不存在,则将表名转大写,再次判断表是否存在
			if(!exist) {
				tables = connection.getMetaData().getTables(connection.getCatalog(), null, table.toUpperCase(), null);
				if(tables ==null || !tables.next()) {
					exist = false;
				}else {
					exist = true;
				}
			}
			
			//若上述判断表都不存在,则进行创建
			if(!exist) {
				JdbcUtils.execute(connection, tableSql);
				LOGGER.info("初始化表成功,表名称: "+table);
			}
		} catch (SQLException e) {
			LOGGER.error("创建  "+table+" 失败!,"+e.getMessage(),e);
		}
	}

	/**
	 * 加载脚本资源
	 * @return
	 * @throws IOException
	 */
	private List<Resource> loadScriptResource() throws IOException {
		List<String> paths = sqlScriptProperties.getPaths();
		
		if(paths == null || paths.size() == 0) {
			return new ArrayList<>();		
		}
		
		List<Resource> scriptResource = new ArrayList<>();
		for (String path : paths) {
			Resource[] resources = resourcePatternResolver.getResources(path);
			
			if(resources == null || resources.length == 0) {
				continue;
			}
			
			for (Resource resource : resources) {
				scriptResource.add(resource);
			}
		}
		
		return scriptResource;
	}

	public SqlScriptProperties getSqlScriptProperties() {
		return sqlScriptProperties;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setSqlScriptDao(SqlScriptDao sqlScriptDao) {
		this.sqlScriptDao = sqlScriptDao;
	}

	public void setEngine(FreemarkerEngine engine) {
		this.engine = engine;
	}
	
}
