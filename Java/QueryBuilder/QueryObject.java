package io.fundae.common.queryBuilder;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Author: Prajyot Walali
 * Email : prajyotwalali.21@gmail.com
 * */
public class QueryObject {
    private ArrayList<String> columns = new ArrayList<>();
    private TreeSet<String> filterConditions = new TreeSet<>();
    private TreeSet<String> groupBy = new TreeSet<>();
    private ArrayList<JoinObject> joins = new ArrayList<>();
    private String fromTable;
    private String fromTableAlias;

    public QueryObject() {
    }

    public QueryObject(ArrayList<String> columns, TreeSet<String> filterConditions, TreeSet<String> groupBy, ArrayList<JoinObject> joins, String fromTable, String fromTableAlias) {
        this.columns = columns;
        this.filterConditions = filterConditions;
        this.groupBy = groupBy;
        this.joins = joins;
        this.fromTable = fromTable;
        this.fromTableAlias = fromTableAlias;
    }

    public static class QueryObjectBuilder{
        private ArrayList<String> columns = new ArrayList<>();
        private TreeSet<String> filterConditions = new TreeSet<>();
        private TreeSet<String> groupBy = new TreeSet<>();
        private ArrayList<JoinObject> joins = new ArrayList<>();
        private String fromTable;
        private String fromTableAlias;

        public QueryObjectBuilder from(String fromTable){
            this.fromTable = fromTable;
            return this;
        }

        public QueryObjectBuilder fromAlias(String fromTableAlias){
            this.fromTableAlias = fromTableAlias;
            return this;
        }

        public QueryObjectBuilder addColumn(String column){
            this.columns.add(column);
            return this;
        }

        public QueryObjectBuilder addFilterCondition(String filterCondition){
            this.filterConditions.add(filterCondition);
            return this;
        }

        public QueryObjectBuilder groupBy(String groupBy){
            this.groupBy.add(groupBy);
            return this;
        }

        public QueryObjectBuilder join(JoinObject joinObject){
            this.joins.add(joinObject);
            return this;
        }

        public QueryObject build(){
            return new QueryObject(columns, filterConditions, groupBy, joins, fromTable, fromTableAlias);
        }
    }

    public static QueryObjectBuilder builder(){
        return new QueryObjectBuilder();
    }

    public String buildQuery(){
        final StringBuilder query = new StringBuilder(" SELECT ");

        /** Select Columns */
        columns.forEach(column -> {
            query.append(" " + column);
            if(columns.lastIndexOf(column) != columns.size()-1){
                query.append(", ");
            }
        });

        /** From table */
        query.append(" FROM ( " + fromTable + " ) " + fromTableAlias + " ");

        /** Joins */
        joins.forEach(joinObject -> {
            query.append(joinObject.buildJoin());
        });

        /** Where Conditions */
        if(!filterConditions.isEmpty()){
            query.append(" WHERE ");
            filterConditions.forEach(filterCondition -> {
                query.append(" " + filterCondition + " ");
                if(!filterConditions.last().equals(filterCondition)) query.append(" AND ");
            });
        }

        /** Group By */
        if(!groupBy.isEmpty()){
            query.append(" GROUP BY ");
            groupBy.forEach(gb -> {
                query.append(" "+ gb + " ");
                if(!groupBy.last().equals(gb)) query.append(", ");
            });
        }

        return query.toString();
    }

}
