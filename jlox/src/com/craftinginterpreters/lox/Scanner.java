package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;      // first character in current lexeme
    private int current = 0;    // current character in source
    private int line = 1;

    private static final Map<String, TokenType> keywords;
    static {
      keywords = new HashMap<>();
      keywords.put("and", AND);
      keywords.put("or", OR);
      keywords.put("true", TRUE);
      keywords.put("false", FALSE);
      keywords.put("class", CLASS);
      keywords.put("fun", FUN);
      keywords.put("for", FOR);
      keywords.put("if", IF);
      keywords.put("else", ELSE);
      keywords.put("nil", NIL);
      keywords.put("print", PRINT);
      keywords.put("return", RETURN);
      keywords.put("this", THIS);
      keywords.put("super", SUPER);
      keywords.put("while", WHILE);
      keywords.put("var", VAR);
    };

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

    // two character lookahead
    private char peekNext() {
        int next = current + 1;
        if (next >= source.length()) return '\0';
        return source.charAt(next);
    }

    private boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c == '_');
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void string() {
        while (peek() != '"' || isAtEnd()) {
            if (peek() == '\n') line += 1;
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string");
            return;
        }

        // the closing "
        advance();
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // look for fractional part
        if (peek() == '.' && isDigit(peekNext())) {
            // consume the '.'
            advance();
            while (isDigit(peek())) advance();
        }

        double value = Double.parseDouble(source.substring(start, current));
        addToken(NUMBER, value);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);

        // check if is an identifier
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void scanToken() {
        // _ _ _ _ c current _ _ _ _
        char c = advance();
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
                    // we use peek instead of current because we want to keep \n character
                    // it will fall through a special case and we'll increment line variable
                    while(peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH); break;
                }
                break;

            // ignore whitespaces
            case ' ':
            case '\t':
            case '\r':
                break;

            case '\n':
                line += 1;
                break;

            // literals
            case '"': string(); break;


            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    // if it starts with a letter or _ could be
                    // an identifier or a reserved word
                    identifier();
                } else {
                    Lox.error(line, "Unexpected character.");
                    break;
                }
        }
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}
