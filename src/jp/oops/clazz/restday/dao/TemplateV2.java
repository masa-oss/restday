package jp.oops.clazz.restday.dao;

import java.util.List;
import java.util.Map;
import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;
import jp.oops.clazz.restday.exception.RestdayException;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi . </div>
 * <div>This software is released under the MIT License.</div>
 */
public class TemplateV2 {

    public Node parseWhere(String sqlTempl) throws RestdayException {

        int findPos = sqlTempl.indexOf("{w");
        if (findPos < 0) {
            return Node.newString(sqlTempl);
        }

        int findPos2 = sqlTempl.indexOf("}", findPos + 1);
        if (findPos2 < 0) {
            throw new ConfigurationSyntaxErrorException("bad sql template", null);
        }

        String where = sqlTempl.substring(findPos + 2, findPos2);

        Node n1 = parseColumn(sqlTempl.substring(0, findPos));

        Node n3 = parseColumn(sqlTempl.substring(findPos2 + 1));
        return Node.newTriple(n1, Node.newWhere(where), n3);
    }

    Node parseColumn(String sqlTempl) throws RestdayException {

        return Node.newString(sqlTempl);
        /*        
        int findPos = sqlTempl.indexOf("{c");
        if (findPos < 0) {
            return Node.newString(sqlTempl);
        }

        int findPos2 = sqlTempl.indexOf("}", findPos + 1);
        if (findPos2 < 0) {
            throw new ConfigurationSyntaxErrorException("bad sql template", null);
        }

        String where = sqlTempl.substring(findPos + 2, findPos2);

        Node n1 = parseOrderBy(sqlTempl.substring(0, findPos));
        
        Node n3 = parseOrderBy(sqlTempl.substring(findPos2+1));
        return Node.newTriple(n1, Node.newWhere(where), n3);
         */
    }

    Node parseOrderBy(String sqlTempl) throws RestdayException {

        return null;
    }

    // Getのクエリーパラメータ処理 (開発中)
    // Get query parameter processing (In development)
    public String procGetParams(Node node, GetParams getParams) {

        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append(node.getLeft().getStr());

        StringBuilder wherePhrase = new StringBuilder();

        Map<String, String> reqMap = getParams.getReqMap();
        List<String> strList = node.getCurrent().getStrList();
        for (String where : strList) {

            int pos = where.indexOf("?");
            int endPos = SqlTemplateResults.searchEnd(where, pos + 1);
            String kwd = where.substring(pos + 1, endPos);

            String getParam = reqMap.get(kwd);

            if (getParam != null) {
                if (wherePhrase.length() > 0) {
                    wherePhrase.append(" AND ");
                }

                wherePhrase.append(where.substring(0, endPos));
                wherePhrase.append(where.substring(endPos));
            }
        }
        if (wherePhrase.length() > 0) {
            sqlStatement.append(" where ");
            sqlStatement.append(wherePhrase);
        }
        sqlStatement.append(node.getRight().getStr());
        return sqlStatement.toString();
    }
}
