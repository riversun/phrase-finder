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

import java.util.regex.Pattern;

/**
 * 
 * 語句の構成要素を判定する
 * 
 * 対象文字列の構成が以下のどれに相当するか判定する
 * 
 * ・全角カタカナ
 * ・全角ひらがな
 * ・全角漢字
 * ・全角数字
 * ・半角英字
 * ・半角数字
 * ・半角英数字
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */

public class PhraseTypeDetector {

  // 全角カタカナ＋音引きのみで構成される語句を検出する正規表現
  private static Pattern zenkakuKatakanaPattern = Pattern.compile("^[ァ-ヶー]+$");

  // 全角ひらがな＋音引きのみで構成される語句を検出する正規表現
  private static Pattern zenkakuHiraganaPattern = Pattern.compile("^[ぁ-んー]+$");

  // 全角数字のみで構成される語句を検出する正規表現
  private static Pattern zenkakuNumericPattern = Pattern.compile("^[０-９]+$");

  // 全角漢字のみで構成される語句を検出する正規表現
  private static Pattern zenkakuKanji = Pattern.compile("^[一-龥]+$");

  // 半角英数字のみで構成される語句を検出する正規表現
  private static Pattern hankakuAlphaNumericPattern = Pattern.compile("^[0-9a-zA-Z\\-\\_]+$");

  // 半角英字のみで構成される語句を検出する正規表現
  private static Pattern hankakuAlphabetPattern = Pattern.compile("^[a-zA-Z\\-\\_]+$");

  // 半角数字のみで構成される語句を検出する正規表現
  private static Pattern hankakuNumericPattern = Pattern.compile("^[0-9]+$");

  /**
   * 文字列が全角カタカナのみで構成されているか否か
   * 
   * @param str
   * @return
   */
  public static boolean isZenkakuKatakanaOnly(String str) {
    return zenkakuKatakanaPattern.matcher(str).matches();
  }

  /**
   * 文字列が全角ひらがなのみで構成されているか否か
   * 
   * @param str
   * @return
   */
  public static boolean isZenkakuHiraganaOnly(String str) {
    return zenkakuHiraganaPattern.matcher(str).matches();
  }

  /**
   * 文字列が半角英数のみで構成されているか否か
   * 
   * @param str
   * @return
   */
  public static boolean isHankakuAlphaNumericOnly(String str) {
    return hankakuAlphaNumericPattern.matcher(str).matches();
  }

  /**
   * 文字列が半角アルファベットのみで構成されているか否か
   * 
   * @param str
   * @return
   */
  public static boolean isHankakuAlphabetOnly(String str) {
    return hankakuAlphabetPattern.matcher(str).matches();
  }

  /**
   * 文字列が半角数字のみで構成されているか否か
   * 
   * @param str
   * @return
   */
  public static boolean isHankakuNumericOnly(String str) {
    return hankakuNumericPattern.matcher(str).matches();
  }

  /**
   * 文字列が全角数字のみで構成されているか否か
   * 
   * @param str
   * @return
   */
  public static boolean isZenkakuNumericOnly(String str) {
    return zenkakuNumericPattern.matcher(str).matches();
  }

  /**
   * 文字列が全角漢字のみで構成されているか否か
   * 
   * @param str
   * @return
   */
  public static boolean isZenkakuKanjiOnly(String str) {
    return zenkakuKanji.matcher(str).matches();
  }

  /**
   * 文字列が全角のみで構成されているか否か
   * 
   * 
   * {@link http://www.alqmst.co.jp/tech/040601.html}
   * 
   * @param s
   * @return
   */
  public static boolean isZenkakuOnly(String s) {
    boolean ret = true;

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if ((c <= '\u007e') || // 英数字
          (c == '\u00a5') || // \記号
          (c == '\u203e') || // ~記号
          (c >= '\uff61' && c <= '\uff9f') // 半角カナ
      ) {
        ret &= false;
      } else {
        // 全角
        ret &= true;
      }

    }
    return ret;
  }

}
