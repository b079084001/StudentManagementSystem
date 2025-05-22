package com.example.utils;



import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/*
 * 代码生成器
 * by xxx
 * @since 2024-11-27
 */
public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }

    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/student?serverTimezone=GMT%2b8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("鹏哥哥") // 设置作者
//                           .fileOverride()  //覆盖已生成文件（改到策略配置下）
                           .enableSwagger() // 开启 swagger 模式
                            .outputDir("D:\\Study\\学生成绩管理系统\\Student-performance-management-system完善版\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder ->
                        builder.parent("com.example") // 设置父包名
                                .moduleName("") // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\Study\\学生成绩管理系统\\Student-performance-management-system完善版\\src\\main\\resources\\mapper\\")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder -> {
                            builder.addInclude("article1") // 设置需要生成的表名
                                    .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                            builder.entityBuilder().enableLombok();
                            builder.controllerBuilder().enableHyphenStyle() //开启驼峰转连字符
                                    .enableRestStyle(); //开启生成@RestController控制器
                            builder.mapperBuilder().enableFileOverride();
                            builder.serviceBuilder().enableFileOverride();
                            builder.controllerBuilder().enableFileOverride();

                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
