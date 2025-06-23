package star.part02.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.part02.model.Recommendation;
import star.part02.model.Rule;
import star.part02.repository.StarRepositoryPart02;

import java.util.UUID;

@Service
public class RecommendationServiceExamplePart2 {
    private final StarRepositoryPart02 repository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceExamplePart2.class);

    public RecommendationServiceExamplePart2(StarRepositoryPart02 repository) {
        this.repository = repository;
    }

    public void createDb(){
        repository.deleteAll();
        Rule[] rules = new Rule[3];

        rules[0] = new Rule(
            "USER_OF",
                new String[]{"CREDIT"},
                true
        );
        rules[1] = new Rule(
                "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
                new String[]{"DEBIT", ">"},
                false
        );
        rules[2] = new Rule(
                "TRANSACTION_SUM_COMPARE",
                new String[]{"DEBIT", "WITHDRAW", ">", "100000"},
                false
        );
        Recommendation recommendation = new Recommendation(
                UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"),
                "Простой кредит",
                """
                        Откройте мир выгодных кредитов с нами!
                        Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.
                        Почему выбирают нас:
                        Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.
                        Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.
                        Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.
                        Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!""",
                rules
        );
        repository.insertRecommendation(recommendation);

        rules[0] = new Rule(
                "USER_OF",
                new String[]{"DEBIT"},
                false
        );
        rules[1] = new Rule(
                "USER_OF",
                new String[]{"INVEST"},
                true
        );
        rules[2] = new Rule(
                "TRANSACTION_SUM_COMPARE",
                new String[]{"SAVING", "DEPOSIT", ">", "1000"},
                false
        );
        recommendation = new Recommendation(
                UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"),
                "Invest 500",
                """
                        Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! 
                        Воспользуйтесь налоговыми льготами и начните инвестировать с умом. 
                        Пополните счет до конца года и получите выгоду в виде вычета на взнос в 
                        следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, 
                        снизить риски и следить за актуальными рыночными тенденциями. 
                        Откройте ИИС сегодня и станьте ближе к финансовой независимости!""",
                rules
        );
        repository.insertRecommendation(recommendation);

        rules[0] = new Rule(
                "USER_OF",
                new String[]{"DEBIT"},
                false
        );
        rules[1] = new Rule(
          "TRANSACTION_SUM_COMPARE",
                new String[]{"SAVING", "DEPOSIT", ">=", "50000"},
                false
        );
        rules[2] = new Rule(
                "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
                new String[]{"DEBIT", ">"},
                false
        );
        recommendation = new Recommendation(
                UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                "Top Saving",
                """
                        Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский 
                        инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. 
                        Больше никаких забытых чеков и потерянных квитанций — всё под контролем!
                        
                        Преимущества «Копилки»:
                        
                        Накопление средств на конкретные цели. Установите лимит и срок накопления, 
                        и банк будет автоматически переводить определенную сумму на ваш счет.
                        
                        Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс 
                        накопления и корректируйте стратегию при необходимости.
                        
                        Безопасность и надежность. Ваши средства находятся под защитой банка, 
                        а доступ к ним возможен только через мобильное приложение или интернет-банкинг.
                        
                        Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""",
                rules
        );
        repository.insertRecommendation(recommendation);

        rules[0] = new Rule(
                "USER_OF",
                new String[]{"DEBIT"},
                false
        );
        rules[1] = new Rule(
                "TRANSACTION_SUM_COMPARE",
                new String[]{"DEBIT", "DEPOSIT", ">=", "50000"},
                false
        );
        rules[2] = new Rule(
                "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
                new String[]{"DEBIT", ">"},
                false
        );
        recommendation = new Recommendation(
                UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                "Top Saving",
                """
                        Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский 
                        инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. 
                        Больше никаких забытых чеков и потерянных квитанций — всё под контролем!
                        
                        Преимущества «Копилки»:
                        
                        Накопление средств на конкретные цели. Установите лимит и срок накопления, 
                        и банк будет автоматически переводить определенную сумму на ваш счет.
                        
                        Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс 
                        накопления и корректируйте стратегию при необходимости.
                        
                        Безопасность и надежность. Ваши средства находятся под защитой банка, 
                        а доступ к ним возможен только через мобильное приложение или интернет-банкинг.
                        
                        Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""",
                rules
        );
        repository.insertRecommendation(recommendation);

    }
}
