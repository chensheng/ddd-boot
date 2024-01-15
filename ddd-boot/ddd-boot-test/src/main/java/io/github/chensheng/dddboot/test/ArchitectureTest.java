package io.github.chensheng.dddboot.test;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Optional;
import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {
    public static void validateDDD(String packageName) {
        JavaClasses projectClasses = new ClassFileImporter().importPackages(packageName);
        doValidateApiLayer(projectClasses, packageName);
        doValidateApplicationLayer(projectClasses, packageName);
        doValidateDomainLayer(projectClasses, packageName);
        doValidateInfrastructureLayer();
    }

    private static void doValidateApiLayer(JavaClasses projectClasses, String packageName) {
        noClasses()
                .that().resideInAnyPackage(packageName + ".api..")
                .should().dependOnClassesThat().resideInAnyPackage(packageName + ".domain..", packageName + ".infrastructure..")
                .because("Api层只能依赖Application层")
                .allowEmptyShould(true)
                .check(projectClasses);
    }

    private static void doValidateDomainLayer(JavaClasses projectClasses, String packageName) {
        noClasses()
                .that().resideInAPackage(packageName + ".domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(packageName + ".infrastructure..", packageName + ".application..", packageName + ".api..")
                .because("Domain层不能依赖api/application/infrastructure层")
                .allowEmptyShould(true)
                .check(projectClasses);

        classes()
                .that().resideInAPackage(packageName + ".domain..")
                .and().haveNameMatching(".*(Entity|Aggregate)")
                .and().areNotMemberClasses()
                .and().areNotInnerClasses()
                .should(entityCondition())
                .because("实体或聚合必须有@Builder和@Getter注解，且不能有@Setter注解")
                .allowEmptyShould(true)
                .check(projectClasses);

        classes()
                .that().resideInAPackage(packageName + ".domain..valueobject..")
                .and().areNotEnums()
                .and().areNotMemberClasses()
                .and().areNotInnerClasses()
                .should(valueObjectCondition())
                .because("值对象必须有@Builder和@Getter注解，且不能有@Setter注解。（或者是枚举类型）")
                .allowEmptyShould(true)
                .check(projectClasses);

        classes()
                .that().resideInAPackage(packageName + ".domain..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .andShould().beInterfaces()
                .because("Domain层的仓储定义必须以Repository结尾，且必须是接口类型")
                .allowEmptyShould(true)
                .check(projectClasses);
    }

    private static void doValidateApplicationLayer(JavaClasses projectClasses, String packageName) {
        classes()
                .that().resideInAPackage(packageName + ".application..service")
                .and().areLocalClasses()
                .should().haveSimpleNameEndingWith("CommandService")
                .orShould().haveSimpleNameEndingWith("QueryService")
                .because("Application层服务的定义必须以QueryService或CommandService结尾")
                .allowEmptyShould(true)
                .check(projectClasses);

        classes()
                .that().resideInAPackage(packageName + ".application..service.impl")
                .and().areLocalClasses()
                .should().haveSimpleNameEndingWith("CommandServiceImpl")
                .orShould().haveSimpleNameEndingWith("QueryServiceImpl")
                .because("Application层服务的实现必须以QueryServiceImpl或CommandServiceImpl结尾")
                .allowEmptyShould(true)
                .check(projectClasses);

        noClasses()
                .that().resideInAPackage(packageName + ".application..service..")
                .and().areLocalClasses()
                .and().haveSimpleNameEndingWith("CommandServiceImpl")
                .should().dependOnClassesThat()
                .resideInAnyPackage(packageName + ".infrastructure..repository..database..", packageName + ".api..")
                .because("Application层CommandServiceImpl不能直接操作数据库")
                .allowEmptyShould(true)
                .check(projectClasses);
    }

    private static void doValidateInfrastructureLayer() {

    }

    private static ArchCondition<JavaClass> entityCondition() {
        return new ArchCondition<JavaClass>("Entity should be annotated with @Builder and @Getter, should not annotated with @Setter. Or be Enum") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                try {
                    item.getMethod("builder");
                } catch (IllegalArgumentException e){
                    events.add(SimpleConditionEvent.violated(item, "Entity " + item.getFullName() + " is not annotated with @Builder"));
                }

                Set<JavaField> fields = item.getAllFields();
                if(fields != null && fields.size() > 0) {
                    for(JavaField field : fields) {
                        String fieldName = field.getName();
                        String setterName = "set" + firstLetterToUpperCase(fieldName);
                        Optional<JavaMethod> setter = item.tryGetMethod(setterName, field.getRawType().getFullName());
                        if(setter.isPresent()) {
                            String message = String.format("Entity %s should not have setter for field: %s", item.getFullName(), field.getName());
                            events.add(SimpleConditionEvent.violated(field, message));
                        }

                        String getterName = "get" + firstLetterToUpperCase(fieldName);
                        Optional<JavaMethod> getter = item.tryGetMethod(getterName);
                        if(!getter.isPresent()) {
                            getterName = "is" + firstLetterToUpperCase(fieldName);
                            getter = item.tryGetMethod(getterName);
                            if(!getter.isPresent()) {
                                String message = String.format("Entity %s should have getter for field: %s", item.getFullName(), field.getName());
                                events.add(SimpleConditionEvent.violated(field, message));
                            }
                        }
                    }
                }
            }
        };
    }

    private static ArchCondition<JavaClass> valueObjectCondition() {
        return new ArchCondition<JavaClass>("Value object should be annotated with @Builder and @Getter, should not annotated with @Setter") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                try {
                    item.getMethod("builder");
                } catch (IllegalArgumentException e){
                    events.add(SimpleConditionEvent.violated(item, "Value object " + item.getFullName() + " is not annotated with @Builder"));
                }

                Set<JavaField> fields = item.getAllFields();
                if(fields != null && fields.size() > 0) {
                    for(JavaField field : fields) {
                        String fieldName = field.getName();
                        String setterName = "set" + firstLetterToUpperCase(fieldName);
                        Optional<JavaMethod> setter = item.tryGetMethod(setterName, field.getRawType().getFullName());
                        if(setter.isPresent()) {
                            String message = String.format("Value object %s should not have setter for field: %s", item.getFullName(), field.getName());
                            events.add(SimpleConditionEvent.violated(field, message));
                        }

                        String getterName = "get" + firstLetterToUpperCase(fieldName);
                        Optional<JavaMethod> getter = item.tryGetMethod(getterName);
                        if(!getter.isPresent()) {
                            getterName = "is" + firstLetterToUpperCase(fieldName);
                            getter = item.tryGetMethod(getterName);
                            if(!getter.isPresent()) {
                                String message = String.format("Value object %s should have getter for field: %s", item.getFullName(), field.getName());
                                events.add(SimpleConditionEvent.violated(field, message));
                            }
                        }
                    }
                }
            }
        };
    }

    private static String firstLetterToUpperCase(String word) {
        if(word == null || word.length() == 0) {
            return word;
        }

        StringBuilder result = new StringBuilder();
        for(int i = 0; i < word.length(); i++) {
            if(i == 0) {
                result.append(String.valueOf(word.charAt(i)).toUpperCase());
            } else {
                result.append(String.valueOf(word.charAt(i)));
            }
        }
        return result.toString();
    }
}
