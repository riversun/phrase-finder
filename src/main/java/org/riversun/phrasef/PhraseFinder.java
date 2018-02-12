/* 
 *  Copyright (c) 2006- Tom Misawa, riversun.org@gmail.com
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a
 *  copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 *  
 */
package org.riversun.phrasef;

import java.util.ArrayList;
import java.util.List;

/**
 * テキストから独立したフレーズを検索するユーティリティ
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class PhraseFinder {

  /**
   * テキストから独立したフレーズを検索する
   * 
   * @param srcText
   * @param phrase
   * @return
   */
  public PhrasefResult findPhrase(String srcText, String phrase) {

    final PhrasefResult result = new PhrasefResult();
    result.posList = new ArrayList<PhrasePos>();

    // 分析モード判定
    // たとえば phraseが”半角英字”だった場合、分析モードは HANKAKU_ALPHABETとなる。
    final PhraseAnalysisMode analysisMode = detectAnalysisMode(phrase);
    result.analysisMode = analysisMode;

    // もとの文字列のどこに対象キーワードがあるのか、その解析処理結果がわかるヒント用文字列
    final StringBuilder sbHint = new StringBuilder();

    boolean loop = true;

    int pointer = 0;

    while (loop) {

      int startIndex = srcText.indexOf(phrase, pointer);

      if (startIndex < 0) {
        loop = false;
        sbHint.append(srcText.substring(pointer, srcText.length()));

        break;
      }

      sbHint.append(srcText.substring(pointer, startIndex));

      final int endIndex = startIndex + phrase.length();

      // 対象キーワードの前にある１文字
      String previousOneChar = null;

      // 対象キーワードの後にある１文字
      String nextOneChar = null;

      if (startIndex - 1 > 0) {
        // - 対象キーワードが先頭よりも後にあった場合
        previousOneChar = srcText.substring(startIndex - 1, startIndex);
      } else {
        // - 対象キーワードが先頭にあった場合
        // previousOneCharはnullとなる
      }
      if (endIndex < srcText.length()) {
        // - 対象キーワードが末尾より前にあった場合
        nextOneChar = srcText.substring(endIndex, endIndex + 1);
      } else {
        // - 対象キーワードが末尾にあった場合
        // nextOneCharはnullとなる
      }

      // 対象キーワードの前にある１文字が、その対象キーワードに対して独立しているか否か
      final boolean isPreviousCharIndependent = isIndependent(previousOneChar, analysisMode);

      // 対象キーワードの次にある１文字が、その対象キーワードに対して独立しているか否か
      final boolean isNextCharIndependent = isIndependent(nextOneChar, analysisMode);

      // 対象キーワードの前後にある１文字が、その対象キーワードに対して独立しているか否か
      final boolean isPrevNextCharIndependent = isPreviousCharIndependent && isNextCharIndependent;

      if (isPrevNextCharIndependent) {
        // - 検出した対象キーワード位置から判断して、そのキーワードが独立していた場合

        final PhrasePos pos = new PhrasePos();
        pos.startIndex = startIndex;
        pos.endIndex = endIndex;

        result.posList.add(pos);

        // 独立したフレーズとして認識された部分をカッコで囲みデコレーションする
        sbHint.append("[" + phrase + "]");
      } else {
        sbHint.append(phrase);
      }
      pointer = endIndex;

    }

    result.isHit = result.posList.size() > 0;
    result.numOfHits = result.posList.size();
    result.hint = sbHint.toString();

    return result;
  }

  /**
   * 指定したphraseがどのような要素群で構成されているか判定する
   * 
   * 例）ひらがなのみ、カタカナのみ、半角数値のみ、半角アルファベットのみ、半角英数のみ、など
   * 
   * @param phrase
   * @return
   */
  private PhraseAnalysisMode detectAnalysisMode(String phrase) {
    if (PhraseTypeDetector.isHankakuNumericOnly(phrase)) {
      return PhraseAnalysisMode.HANKAKU_NUMERIC_ONLY;
    } else if (PhraseTypeDetector.isHankakuAlphabetOnly(phrase)) {
      return PhraseAnalysisMode.HANKAKU_ALPHABET_ONLY;
    } else if (PhraseTypeDetector.isHankakuAlphaNumericOnly(phrase)) {
      return PhraseAnalysisMode.HANKAKU_ALPHA_NUMERIC;
    } else if (PhraseTypeDetector.isZenkakuHiraganaOnly(phrase)) {
      return PhraseAnalysisMode.ZENKAKU_HIRAGANA;
    } else if (PhraseTypeDetector.isZenkakuKatakanaOnly(phrase)) {
      return PhraseAnalysisMode.ZENKAKU_KATAKANA;
    } else if (PhraseTypeDetector.isZenkakuNumericOnly(phrase)) {
      return PhraseAnalysisMode.ZENKAKU_NUMERIC_ONLY;
    } else if (PhraseTypeDetector.isZenkakuKanjiOnly(phrase)) {
      return PhraseAnalysisMode.ZENKAKU_KANJI;
    }
    return PhraseAnalysisMode.UNKNOWN;
  }

  /**
   * 指定した文字列が、指定した分析モードに対して”独立”か否かを判定する
   * 
   * @param phrase
   * @param analysisMode
   * @return
   */
  private boolean isIndependent(String phrase, PhraseAnalysisMode analysisMode) {

    if (phrase == null) {
      return true;
    }

    switch (analysisMode) {
    case HANKAKU_ALPHABET_ONLY:
      if (PhraseTypeDetector.isHankakuAlphabetOnly(phrase)) {
        return false;
      } else {
        return true;
      }
    case HANKAKU_NUMERIC_ONLY:
      if (PhraseTypeDetector.isHankakuNumericOnly(phrase) || PhraseTypeDetector.isHankakuAlphabetOnly(phrase)) {
        return false;
      } else {
        return true;
      }
    case HANKAKU_ALPHA_NUMERIC:
      if (PhraseTypeDetector.isHankakuAlphaNumericOnly(phrase)) {
        // - 対象モードが半角英数検出モードで、指定した文字列が半角英数なら、従属なのでfalse
        return false;
      } else {
        return true;
      }
    case ZENKAKU_KATAKANA:
      if (PhraseTypeDetector.isZenkakuKatakanaOnly(phrase)) {
        // - 対象モードが全角カタカナ検出モードで、指定した文字列が全角カタカナなら、従属なのでfalse
        return false;
      } else {
        return true;
      }
    case ZENKAKU_HIRAGANA:
      if (PhraseTypeDetector.isZenkakuHiraganaOnly(phrase)) {
        return false;
      } else {
        return true;
      }
    case ZENKAKU_NUMERIC_ONLY:
      if (PhraseTypeDetector.isZenkakuNumericOnly(phrase)) {
        return false;
      } else {
        return true;
      }
    case ZENKAKU_KANJI:
      if (PhraseTypeDetector.isZenkakuKanjiOnly(phrase)) {
        return false;
      } else {
        return true;
      }
    default:
      return false;
    }
  }

  /**
   * 分析モード
   */
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

  /**
   * 分析結果
   */
  public static class PhrasefResult {

    /**
     * テキスト分析モード
     */
    public PhraseAnalysisMode analysisMode;

    /**
     * 検索の結果、さがしたいテキストが見つかった否か
     */
    public boolean isHit;

    /**
     * 検索結果のヒット数
     */
    public int numOfHits;

    /**
     * 分析結果のヒント
     */
    public String hint;

    /**
     * 検索対象文字列の位置
     */
    public List<PhrasePos> posList;

    @Override
    public String toString() {
      return "PhrasefResult [analysisMode=" + analysisMode + ", isHit=" + isHit + ", numOfHits=" + numOfHits + ", hint=" + hint + ", posList=" + posList + "]";
    }

  }

  /**
   * 文字列出現位置
   */
  public static class PhrasePos {
    public int startIndex;
    public int endIndex;

    @Override
    public String toString() {
      return "PhrasePos [startIndex=" + startIndex + ", endIndex=" + endIndex + "]";
    }

  }
}
