package com.craftinginterpreters.lox;

/**
 * Expr class
 */
abstract class Expr {
    static class Binary extends Expr {
        final Expr left;
        final Expr right;
        final Token operator;

        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }
}
