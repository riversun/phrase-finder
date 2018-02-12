# 概要
- 文書の中から、特定の文字列をフレーズ検索するユーティリティ  
- 語のつながりを抽出するため、 String#indexOfやString#containsでは不可能だったフレーズ単位での検索が実行できる  

It is licensed under [MIT](https://opensource.org/licenses/MIT).

# 例
以下の文章から **free**という文字列をフレーズ検索する

文例：**I'm a freelancer but I'm not free.**  
結果：**I'm a freelancer but I'm not [free].**  
「freelancer」にあるfreeは検索しない。freelancerというかたまりであるから。

# コード例

```java
import org.riversun.phrasef.PhraseFinder;
import org.riversun.phrasef.PhraseFinder.PhrasefResult;

public class Example2 {

  public static void main(String[] args) {
    final PhraseFinder obj = new PhraseFinder();
    final String TEXT = "I'm a freelancer but I'm not free.";
    final String SEARCH_PHRASE = "free";
    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);
    System.out.println(result);
  }
}
```


実行結果
```
PhrasefResult [analysisMode=HANKAKU_ALPHABET_ONLY, isHit=true, numOfHits=1, hint=I'm a freelancer but I'm not [free]., posList=[PhrasePos [startIndex=29, endIndex=33]]]

```

# 対応しているフレーズの種類
フレーズ（語句）の種類は以下のenumに示す通り

```java
  public enum PhraseAnalysisMode {
    ZENKAKU_KATAKANA, // 全角カタカナ専用分析モード
    ZENKAKU_HIRAGANA, // 全角ひらがな専用分析モード
    ZENKAKU_KANJI, // 全角漢字専用分析モード
    ZENKAKU_NUMERIC_ONLY, // 全角数字専用分析モード
    HANKAKU_NUMERIC_ONLY, // 半角数字専用分析モード
    HANKAKU_ALPHABET_ONLY, // 半角英字専用分析モード
    HANKAKU_ALPHA_NUMERIC, // 半角英数字専用分析モード
    UNKNOWN
  };
 ```
