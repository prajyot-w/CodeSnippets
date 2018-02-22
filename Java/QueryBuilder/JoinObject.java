package io.fundae.common.queryBuilder;

import java.util.TreeSet;

/**
 * Author: Prajyot Walali
 * Email : prajyotwalali.21@gmail.com
 * */
public class JoinObject {
    private String tableName;
    private String aliasName;
    private JoinType joinType;
    private TreeSet<String> joinConditions = new TreeSet<>();

    public JoinObject() {
    }

    public JoinObject(String tableName, String aliasName, JoinType joinType, TreeSet<String> joinConditions) {
        this.tableName = tableName;
        this.aliasName = aliasName;
        this.joinType = joinType;
        this.joinConditions = joinConditions;
    }

    public static class JoinObectBuilder {
        private String tableName;
        private String aliasName;
        private JoinType joinType;
        private TreeSet<String> joinConditions = new TreeSet<>();

        public JoinObectBuilder tableName(String tableName){
            this.tableName = tableName;
            return this;
        }

        public JoinObectBuilder aliasName(String aliasName){
            this.aliasName = aliasName;
            return this;
        }

        public JoinObectBuilder joinType(JoinType joinType){
            this.joinType = joinType;
            return this;
        }

        public JoinObectBuilder addCondition(String condition){
            this.joinConditions.add(condition);
            return this;
        }

        public JoinObject build(){
            return new JoinObject(tableName, aliasName, joinType, joinConditions);
        }
    }

    public static JoinObectBuilder builder(){
        return new JoinObectBuilder();
    }

    public String buildJoin(){
        final StringBuilder join = new StringBuilder();
        switch (this.joinType){
            case INNER:
                join.append(" INNER ");
                break;
            case LEFT:
                join.append(" LEFT ");
                break;
            case RIGHT:
                join.append(" RIGHT ");
                break;
            default:
                join.append(" INNER ");
                break;
        }
        join.append(" JOIN ");
        join.append( "( "+tableName+" ) "+ aliasName + " ON " );
        joinConditions.forEach(s -> {
            join.append(" "+s+" ");
            if(!joinConditions.last().equals(s)){
                join.append(" AND ");
            }
        });
        return join.toString();
    }

    public static void main(String[] args) {
        System.out.println(
                JoinObject.builder()
                        .tableName("data.date_master")
                        .aliasName("d")
                        .addCondition("t.period_type = d.period_type")
                        .addCondition("t.period = d.period_date")
                        .joinType(JoinType.INNER)
                        .build().buildJoin()
        );
    }
}
