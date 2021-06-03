package cn.wulin.brace.demo.comprehensive.druid_parser_sql;

import java.util.List;

import org.junit.Test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcConstants;

/**
 * 使用druid来解析SQL语句
 * @author wulin
 *
 */
public class DruidParserSql {

	private static String sql = "select p.personName as personName,u.username,u.password from sys_user u left join sys_person p where p.orgId = '123' and u.username = 'admin'";
	public static void main(String[] args) {
		
		SQLStatementParser createSQLStatementParser = SQLParserUtils.createSQLStatementParser(sql, JdbcConstants.MYSQL);
		SQLSelectStatement parseStatement =(SQLSelectStatement) createSQLStatementParser.parseStatement();
		
		SQLSelect select = parseStatement.getSelect();
		SQLSelectQueryBlock query = (SQLSelectQueryBlock)select.getQuery();
		SQLTableSource from = query.getFrom();
		SQLExpr where = query.getWhere();
		List<SQLSelectItem> selectList = query.getSelectList();
		
		System.out.println();
		
		
		List<SQLStatement> parseStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
		
//		System.out.println(parseStatements);
		
	}
	
	@Test
	public void formatMySql() {
		System.out.println(SQLUtils.formatMySql(sql));
	}
}
