package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;

import static com.craftinginterpreters.lox.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;      // first character in current lexeme
    private int current = 0;    // current character in source
    private int line = 1;

    Scanner(String source) {
        // raw source code
        this.source = source;
    }

    List<Token> scanTokens() {
        while(!isAtEnd()) {
            // We're at the beginning of the next lexeme
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));

        return tokens;
    }

    private char advance() {
        // consume next character,
        // it will consume even the unexpected one thus
        // the scanner don't get stuck in an infinite loop
        current += 1;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type, Object literal) {
        String text = this.source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private boolean match(char expected) {
        // compare current with expected (which is current+1 because of advance())
        if (isAtEnd()) return false;
        if (this.source.charAt(current) != expected) return false;
        current += 1;
        return true;
    }

    // one character lookahead
    private char peek() {
        if (current >= source.length()) return '\0';
        return source.charAt(current);
    }

    private void scanToken() {
        char c = advance();
        // c = current - 1;
        switch(c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;

            // special cases for token that might have
            // more than one characters (e.g. ! vs !=)
            case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
            case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
            case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;

            // slash is a special case because comments start with a / too :)
            case '/':
                if (match('/')) {
                    // this is a comment, consume characters until the end of the line
                    while(peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH); break;
                }
                break;

            default:
                Lox.error(line, "Unexpected character.");
                break;
        }
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}
