package com.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Tool for generate AST classes
 */
public class GenerateAst {
    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output_file>");
            System.exit(1);
        }

        String outputDir = args[0];

        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
                ));

    }

    private static void defineAst(String outputDir, String baseClass, List<String> types) throws IOException {
        String path = outputDir + "/" + baseClass + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.craftinginterpreters.lox;");
        writer.println("");
        writer.println("import java.util.List");
        writer.println("");
        writer.println("abstract class" + baseClass + " {");
        for (String type : types) {
           defineType(type, writer, baseClass);
        }
        writer.println("}");
        writer.close();
    }

    private static void defineType(String type, PrintWriter writer, String baseClass) {
        String className = type.split(":")[0].trim();
        String fieldsList = type.split(":")[1].trim();
        String fields[] = fieldsList.split(",");
        writer.println("static class " + className + "extends " + baseClass + "{");

        writer.println("    " + className + "(" + fieldsList + "){");
        for (String field: fields) {
            writer.println("this." + field.split(" ")[1] + " = " + field.split(" ")[1]);
        }
        writer.println("}");

        for (String field: fields) {
            writer.println("    final" + field + ";");
        }
        writer.println("}");
        writer.println("");
    }
}
