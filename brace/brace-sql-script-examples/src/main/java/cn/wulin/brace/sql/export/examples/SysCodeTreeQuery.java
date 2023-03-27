package cn.wulin.brace.sql.export.examples;
import cn.hutool.core.text.StrFormatter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典菜单导出工具类
 * @author wulin
 *
 */
public class SysCodeTreeQuery {
    public static void main(String[] args) {
        // 配置 DruidDataSource
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.2.81/ebmp_cas");
        dataSource.setUsername("ebmp_cas");
        dataSource.setPassword("123456789");

        // 创建 JdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // 执行树型查询
        List<Map<String, Object>> sysCodes = fetchSysCodeHierarchy(jdbcTemplate, "3dd489c477574dc699c71c16d34629f8");

        // 生成 INSERT 语句
        generateInsertStatements(sysCodes);
    }

    private static List<Map<String, Object>> fetchSysCodeHierarchy(JdbcTemplate jdbcTemplate, String rootId) {
        List<Map<String, Object>> allSysCodes = new ArrayList<>();
        List<Map<String, Object>> currentLevelSysCodes = new ArrayList<>();
        Map<String, Object> root = new HashMap<>();
        root.put("ID", rootId);
        root.put("PARENTID", null);
        currentLevelSysCodes.add(root);

        while (!currentLevelSysCodes.isEmpty()) {
            List<Map<String, Object>> nextLevelSysCodes = new ArrayList<>();
            for (Map<String, Object> sysCode : currentLevelSysCodes) {
                String query = "SELECT * FROM sys_code WHERE PARENTID = ?";
                List<Map<String, Object>> children = jdbcTemplate.query(query, new Object[]{sysCode.get("ID")}, new SysCodeRowMapper());
                nextLevelSysCodes.addAll(children);
            }
            allSysCodes.addAll(currentLevelSysCodes);
            currentLevelSysCodes = nextLevelSysCodes;
        }
        return allSysCodes;
    }

    private static void generateInsertStatements(List<Map<String, Object>> sysCodes) {
        String insertTemplate = "INSERT INTO sys_code_copy (ID, CHINESECHAR, CODE, CODENAME, ISDEFAULT, MAPPINGCODE, MEMOVALUE, PXNUM, PARENTID, IS_DEPRECATED) VALUES ('{}', '{}', '{}', '{}', {}, '{}', '{}', {}, '{}', {});";

        for (Map<String, Object> sysCode : sysCodes) {
            System.out.println(StrFormatter.format(insertTemplate, sysCode.get("ID"), sysCode.get("CHINESECHAR"), sysCode.get("CODE"), sysCode.get("CODENAME"), sysCode.get("ISDEFAULT"), sysCode.get("MAPPINGCODE"), sysCode.get("MEMOVALUE"), sysCode
			        .get("PXNUM"), sysCode.get("PARENTID"), sysCode.get("IS_DEPRECATED")));
    }
}

private static class SysCodeRowMapper implements RowMapper<Map<String, Object>> {
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> sysCode = new HashMap<>();
        sysCode.put("ID", rs.getString("ID"));
        sysCode.put("CHINESECHAR", rs.getString("CHINESECHAR"));
        sysCode.put("CODE", rs.getString("CODE"));
        sysCode.put("CODENAME", rs.getString("CODENAME"));
        sysCode.put("ISDEFAULT", rs.getBigDecimal("ISDEFAULT"));
        sysCode.put("MAPPINGCODE", rs.getString("MAPPINGCODE"));
        sysCode.put("MEMOVALUE", rs.getString("MEMOVALUE"));
        sysCode.put("PXNUM", rs.getBigDecimal("PXNUM"));
        sysCode.put("PARENTID", rs.getString("PARENTID"));
        sysCode.put("IS_DEPRECATED", rs.getBigDecimal("IS_DEPRECATED"));
        return sysCode;
    }
}
}