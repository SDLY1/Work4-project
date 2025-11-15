package com.example.wwork4.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 敏感词检测字典树（静态工具类，可直接类名调用）
 * 支持：大小写不敏感、全角半角转换、多线程安全、批量导入、敏感词检测/替换
 */
public class SensitiveWordTrieUtils {

    // -------------------------- 静态成员变量（全局唯一） --------------------------
    // 根节点（静态常量，类加载时初始化）
    private static final TrieNode root;

    // 静态配置变量（全局统一，如需修改可通过静态setter）
    private static boolean ignoreCase = true;
    private static boolean convertFullHalfWidth = true;
    private static char replaceChar = '*';

    // -------------------------- 静态代码块（类加载时初始化，只执行一次） --------------------------
    static {
        // 初始化根节点
        root = new TrieNode();
        // 初始化敏感词（可选：这里可加载默认敏感词，或留空后续通过 addWords 动态添加）
        List<String> defaultSensitiveWords = Arrays.asList(
                "赌博", "色情", "暴力", "毒品", "网络诈骗", "XXX"
        );
        addWords(defaultSensitiveWords);
//        System.out.println("敏感词字典树初始化完成，默认加载敏感词数量：" + defaultSensitiveWords.size());
    }

    // -------------------------- 私有构造函数（禁止外部 new 实例） --------------------------
    private SensitiveWordTrieUtils() {}

    /**
     * 字典树节点类（线程安全）
     */
    private static class TrieNode {
        private final Map<Character, TrieNode> children = new ConcurrentHashMap<>();
        private volatile boolean isEnd = false;

        public Map<Character, TrieNode> getChildren() {
            return children;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }
    }

    // -------------------------- 静态配置方法（全局生效） --------------------------
    public static void setIgnoreCase(boolean ignoreCase) {
        SensitiveWordTrieUtils.ignoreCase = ignoreCase;
    }

    public static void setConvertFullHalfWidth(boolean convertFullHalfWidth) {
        SensitiveWordTrieUtils.convertFullHalfWidth = convertFullHalfWidth;
    }

    public static void setReplaceChar(char replaceChar) {
        SensitiveWordTrieUtils.replaceChar = replaceChar;
    }

    // -------------------------- 静态工具方法：全角半角转换 --------------------------
    private static char fullToHalf(char c) {
        if (c == '\u3000') {
            return '\u0020';
        }
        if (c >= '\uFF01' && c <= '\uFF5E') {
            return (char) (c - 65248);
        }
        return c;
    }

    private static char normalizeChar(char c) {
        if (convertFullHalfWidth) {
            c = fullToHalf(c);
        }
        if (ignoreCase) {
            c = Character.toLowerCase(c);
        }
        return c;
    }

    // -------------------------- 静态敏感词操作方法 --------------------------
    public static void addWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("敏感词不能为空或空白");
        }

        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            char normalizedChar = normalizeChar(c);
            current.getChildren().computeIfAbsent(normalizedChar, k -> new TrieNode());
            current = current.getChildren().get(normalizedChar);
        }
        current.setEnd(true);
    }

    public static void addWords(Collection<String> words) {
        if (words == null || words.isEmpty()) {
            return;
        }
        for (String word : words) {
            addWord(word);
        }
    }

    public static boolean removeWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("敏感词不能为空或空白");
        }

        TrieNode current = root;
        Deque<TrieNode> path = new LinkedList<>();
        path.push(current);

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            char normalizedChar = normalizeChar(c);
            current = current.getChildren().get(normalizedChar);
            if (current == null) {
                return false;
            }
            path.push(current);
        }

        if (!current.isEnd()) {
            return false;
        }

        current.setEnd(false);

        TrieNode parent;
        char lastChar = normalizeChar(word.charAt(word.length() - 1));
        path.pop();

        while (!path.isEmpty()) {
            parent = path.pop();
            if (!current.isEnd() && current.getChildren().isEmpty()) {
                parent.getChildren().remove(lastChar);
            } else {
                break;
            }
            if (word.length() > 1) {
                lastChar = normalizeChar(word.charAt(path.size() - 1));
            }
            current = parent;
        }

        return true;
    }

    public static void clear() {
        root.getChildren().clear();
    }

    // -------------------------- 静态核心功能方法 --------------------------
    public static Set<String> detectSensitiveWords(String text) {
        Set<String> result = new HashSet<>();
        if (text == null || text.isEmpty()) {
            return result;
        }

        char[] chars = text.toCharArray();
        int textLength = chars.length;

        for (int i = 0; i < textLength; i++) {
            TrieNode current = root;
            StringBuilder sensitiveWord = new StringBuilder();

            for (int j = i; j < textLength; j++) {
                char c = chars[j];
                char normalizedChar = normalizeChar(c);

                if (!current.getChildren().containsKey(normalizedChar)) {
                    break;
                }

                sensitiveWord.append(c);
                current = current.getChildren().get(normalizedChar);

                if (current.isEnd()) {
                    result.add(sensitiveWord.toString());
                }
            }
        }

        return result;
    }

    public static String replaceSensitiveWords(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        char[] chars = text.toCharArray();
        int textLength = chars.length;
        boolean[] replaceFlags = new boolean[textLength];

        for (int i = 0; i < textLength; i++) {
            TrieNode current = root;
            int maxMatchIndex = -1;

            for (int j = i; j < textLength; j++) {
                char c = chars[j];
                char normalizedChar = normalizeChar(c);

                if (!current.getChildren().containsKey(normalizedChar)) {
                    break;
                }

                current = current.getChildren().get(normalizedChar);
                if (current.isEnd()) {
                    maxMatchIndex = j;
                }
            }

            if (maxMatchIndex != -1) {
                for (int k = i; k <= maxMatchIndex; k++) {
                    replaceFlags[k] = true;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < textLength; i++) {
            result.append(replaceFlags[i] ? replaceChar : chars[i]);
        }

        return result.toString();
    }

    public static boolean containsSensitiveWord(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        char[] chars = text.toCharArray();
        int textLength = chars.length;

        for (int i = 0; i < textLength; i++) {
            TrieNode current = root;

            for (int j = i; j < textLength; j++) {
                char c = chars[j];
                char normalizedChar = normalizeChar(c);

                if (!current.getChildren().containsKey(normalizedChar)) {
                    break;
                }

                current = current.getChildren().get(normalizedChar);
                if (current.isEnd()) {
                    return true;
                }
            }
        }

        return false;
    }



}