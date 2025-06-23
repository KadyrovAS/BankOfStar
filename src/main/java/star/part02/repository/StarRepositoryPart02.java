package star.part02.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import star.part02.model.Transaction;
import star.part02.model.Recommendation;
import star.part02.model.Rule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class StarRepositoryPart02{
    private final JdbcTemplate transactionsJdbcTemplate;
    private final JdbcTemplate rulesJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(StarRepositoryPart02.class);

    public StarRepositoryPart02(@Qualifier("transactionsJdbcTemplate") JdbcTemplate transactionsJdbcTemplate,
                                @Qualifier("rulesJdbcTemplatePart02") JdbcTemplate rulesJdbcTemplate) {
        this.transactionsJdbcTemplate = transactionsJdbcTemplate;
        this.rulesJdbcTemplate = rulesJdbcTemplate;
    }

    @Cacheable(value = "rules", key = "#id")
    public Rule findRuleById(UUID id) {
        String sql = "SELECT * FROM RULE WHERE ID = ?";
        try {
            return rulesJdbcTemplate.queryForObject(
                    sql,
                    this::mapRowToRule,
                    id.toString()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private void insertRule(Rule rule, UUID id) {
        Optional<String> argument01;
        Optional<String> argument02 = Optional.empty();
        Optional<String> argument03 = Optional.empty();
        Optional<String> argument04 = Optional.empty();
        String sql = "INSERT INTO RULE (ID, QUERY, ARGUMENT01, ARGUMENT02, ARGUMENT03, ARGUMENT04, NEGATIVE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        argument01 = Optional.ofNullable(rule.getArguments()[0]);
        if (rule.getArguments().length > 1) {
            argument02 = Optional.ofNullable(rule.getArguments()[1]);
        }
        if (rule.getArguments().length > 2) {
            argument03 = Optional.ofNullable(rule.getArguments()[2]);
        }
        if (rule.getArguments().length > 3) {
            argument04 = Optional.ofNullable(rule.getArguments()[3]);
        }

        rulesJdbcTemplate.update(sql,
                id.toString(),
                rule.getQuery(),
                argument01.orElse(""),
                argument02.orElse(""),
                argument03.orElse(""),
                argument04.orElse(""),
                rule.isNegative());
    }

    @Cacheable(value = "recommendations")
    public List<Recommendation> findAllRecommendations() {
        String sql = "SELECT ID FROM RECOMMENDATION";
        List<UUID> recommendationsId =
                rulesJdbcTemplate.query(
                        sql,
                        (rs, rowNum) -> UUID.fromString(rs.getString("ID"))
                );


        List<Recommendation> recommendations = new LinkedList<>();
        for (UUID id : recommendationsId) {
            recommendations.add(findRecommendationById(id));
        }
        return recommendations;
    }

    @Cacheable(value = "recommendation", key = "#id")
    private Recommendation findRecommendationById(UUID id) {
        String sql = "SELECT RULE_ID FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";

        List<UUID> rulesId = rulesJdbcTemplate.query(
                sql,
                (rs, rowNum) -> UUID.fromString(rs.getString("RULE_ID")),
                id.toString()
        );

        if (rulesId.isEmpty()) {
            return null;
        }

        int size = rulesId.size();
        Rule[] rules = new Rule[size];
        for (int i = 0; i < size; i++) {
            rules[i] = findRuleById(rulesId.get(i));
        }

        sql = "SELECT * FROM RECOMMENDATION WHERE ID = ?";
        return rulesJdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new Recommendation(
                        UUID.fromString(rs.getString("CONTRACT_ID")),
                        rs.getString("NAME"),
                        rs.getString("TEXT"),
                        rules
                ),
                id.toString()
        );
    }

    public void insertRecommendation(Recommendation recommendation) {
        UUID ruleId;
        UUID id = UUID.randomUUID();

        String sql = "INSERT INTO RECOMMENDATION " +
                "(ID, CONTRACT_ID, NAME, TEXT) " +
                "VALUES (?, ?, ?, ?)";
        rulesJdbcTemplate.update(
                sql,
                id.toString(),
                recommendation.getId().toString(),
                recommendation.getName(),
                recommendation.getText()
        );

        sql = "INSERT INTO RULE_TO_RECOMMENDATION " +
                "(RECOMMENDATION_ID, RULE_ID) " +
                "VALUES (?, ?)";

        for (Rule rule : recommendation.getRules()) {
            ruleId = UUID.randomUUID();
            rulesJdbcTemplate.update(
                    sql,
                    id.toString(),
                    ruleId.toString()
            );
            insertRule(rule, ruleId);
        }
    }

    private void deleteRule(UUID id) {
        String sql = "DELETE FROM RULE WHERE ID = ?";
        rulesJdbcTemplate.update(sql, id.toString());
    }

    public void deleteRecommendation(UUID contractId) {
        logger.info("deleteRecommendation: contractId = '{}'", contractId);
        String sql = "SELECT * FROM RECOMMENDATION WHERE CONTRACT_ID = ?";
        List<UUID> listId = rulesJdbcTemplate.query(
                sql,
                (rs, rowNum) -> UUID.fromString(rs.getString("ID")),
                contractId.toString()
        );
        for (UUID id : listId) {
            sql = "SELECT * FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";
            List<UUID> rulesId = rulesJdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> UUID.fromString(rs.getString("RULE_ID")),
                    id.toString()
            );

            sql = "DELETE FROM RULE_TO_RECOMMENDATION WHERE RECOMMENDATION_ID = ?";
            rulesJdbcTemplate.update(sql, id);

            for (UUID ruleId : rulesId) {
                deleteRule(ruleId);
            }
        }
        sql = "DELETE FROM RECOMMENDATION WHERE CONTRACT_ID = ?";
        rulesJdbcTemplate.update(sql, contractId);
    }

    private record RecommendationRecord(String name, String text, String query){
    }


    private Rule mapRowToRule(ResultSet rs, int rowNum) throws SQLException {
        String query = rs.getString("QUERY");
        boolean negative = rs.getBoolean("NEGATIVE");

        String[] arguments = new String[4];
        arguments[0] = rs.getString("ARGUMENT01");
        arguments[1] = rs.getString("ARGUMENT02");
        arguments[2] = rs.getString("ARGUMENT03");
        arguments[3] = rs.getString("ARGUMENT04");

        return new Rule(query, arguments, negative);
    }

    @Cacheable(value = "transactions", key = "#id")
    public List<Transaction> getAmountsByTypes(UUID id) {
        String sql = "SELECT PRODUCTS.TYPE AS PRODUCT_TYPE, TRANSACTIONS.TYPE AS TRANSACTION_TYPE, " +
                "SUM(AMOUNT) AS AMOUNT, COUNT(AMOUNT) AS COUNT FROM " +
                "TRANSACTIONS LEFT JOIN PRODUCTS ON PRODUCT_ID = PRODUCTS.ID WHERE USER_ID = ? " +
                "GROUP BY TRANSACTIONS.TYPE, PRODUCT_TYPE";

        return transactionsJdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Transaction(
                        rs.getInt("AMOUNT"),
                        rs.getInt("COUNT"),
                        rs.getString("TRANSACTION_TYPE"),
                        rs.getString("PRODUCT_TYPE")
                ),
                id.toString()
        );
    }

    public void deleteAll() {
        rulesJdbcTemplate.update("DELETE FROM RECOMMENDATION");
        rulesJdbcTemplate.update("DELETE FROM RULE");
        rulesJdbcTemplate.update("DELETE FROM RULE_TO_RECOMMENDATION");
    }

}
