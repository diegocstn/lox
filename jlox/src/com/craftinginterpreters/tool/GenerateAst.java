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
        writer.println("import java.util.List;");
        writer.println("");
        writer.println("abstract class " + baseClass + " {");

        // visitor interface
        defineVisitor(types, baseClass, writer);

        // accept method
        writer.println("");
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");
        writer.println("");

        // AST classes
        for (String type : types) {
            defineType(type, writer, baseClass);
        }

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(List<String> types, String baseClass, PrintWriter writer) {
        writer.println("    interface Visitor<R> {");
        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("        R visit" + typeName + baseClass + "(" + typeName + " " + baseClass.toLowerCase() + ");");
        }
        writer.println("    }");
    }

    private static void defineType(String type, PrintWriter writer, String baseClass) {
        String className = type.split(":")[0].trim();
        String fieldsList = type.split(":")[1].trim();
        writer.println("    static class " + className + " extends " + baseClass + " {");

        // constructor
        writer.println("        " + className + "(" + fieldsList + "){");
        String fields[] = fieldsList.split(", ");

        for (String field: fields) {
            String fieldName = field.split(" ")[1].trim();
            writer.println(
                    "            this." + fieldName + " = " + fieldName + ";"
            );
        }
        writer.println("        }");

        for (String field: fields) {
            writer.println("        final " + field + ";");
        }

        // concrete accept method
        writer.println("");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseClass + "(this);");
        writer.println("        }");

        writer.println("    }");
        writer.println("");
    }
}
