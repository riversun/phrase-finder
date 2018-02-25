package org.riversun.phrasef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.riversun.phrasef.PhraseFinder.PhraseAnalysisMode;
import org.riversun.phrasef.PhraseFinder.PhrasefResult;
import org.riversun.phrasef.PhraseFinder.PhrasefResultSet;

/**
 * Test for Phrase finder class <br>
 * <br>
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class TestPhraseFinder {

  @Rule
  public TestName name = new TestName();

  private PhraseFinder obj = new PhraseFinder();

  @Test
  public void test_get_halfsize_alphabet_from_japanese_sentence_1() throws Exception {
    // テストの期待動作：独立したフレーズ「DENT」のみに反応し、「PRESIDENT」や「DENTAL」に含まれる"DENT"という文字列に反応しないこと
    final String TEXT = "週刊PRESIDENTオンラインの記事では仮想通貨DENTに関する記事をいつでも閲覧できます。DENTはDENTAL COINとは別モノですので要注意です。";
    final String SEARCH_PHRASE = "DENT";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(PhraseAnalysisMode.HANKAKU_ALPHABET_ONLY, result.analysisMode);
    assertEquals(2, result.numOfHits);
    assertEquals("週刊PRESIDENTオンラインの記事では仮想通貨[DENT]に関する記事をいつでも閲覧できます。[DENT]はDENTAL COINとは別モノですので要注意です。", result.hint);
    assertEquals(25, result.posList.get(0).startIndex);
    assertEquals(47, result.posList.get(1).startIndex);

  }

  @Test
  public void test_get_halfsize_alphabet_from_japanese_sentence_2() throws Exception {
    // テストの期待動作：独立したフレーズ「DENT」のみに反応し、「PRESIDENT」や「DENTAL」に含まれる"DENT"という文字列に反応しないこと
    final String TEXT = "DENT 週刊PRESIDENTオンラインの記事では仮想通貨DENTに関する記事をいつでも閲覧できます。DENTはDENTAL COINとは別モノですので要注意です。";
    final String SEARCH_PHRASE = "DENT";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(PhraseAnalysisMode.HANKAKU_ALPHABET_ONLY, result.analysisMode);
    assertEquals(3, result.numOfHits);
    assertEquals("[DENT] 週刊PRESIDENTオンラインの記事では仮想通貨[DENT]に関する記事をいつでも閲覧できます。[DENT]はDENTAL COINとは別モノですので要注意です。", result.hint);
    assertEquals(0, result.posList.get(0).startIndex);
    assertEquals(30, result.posList.get(1).startIndex);
    assertEquals(52, result.posList.get(2).startIndex);

  }

  @Test
  public void test_get_halfsize_alphabet_from_japanese_sentence_3() throws Exception {
    // テストの期待動作：独立したフレーズ「DENT」のみに反応し、「PRESIDENT」や「DENTAL」に含まれる"DENT"という文字列に反応しないこと
    final String TEXT = "DENT 週刊PRESIDENTオンラインの記事では仮想通貨DENTに関する記事をいつでも閲覧できます。DENTはDENTAL COINとは別モノですので要注意です。DENT";
    final String SEARCH_PHRASE = "DENT";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(PhraseAnalysisMode.HANKAKU_ALPHABET_ONLY, result.analysisMode);
    assertEquals(4, result.numOfHits);
    assertEquals("[DENT] 週刊PRESIDENTオンラインの記事では仮想通貨[DENT]に関する記事をいつでも閲覧できます。[DENT]はDENTAL COINとは別モノですので要注意です。[DENT]", result.hint);
    assertEquals(0, result.posList.get(0).startIndex);
    assertEquals(30, result.posList.get(1).startIndex);
    assertEquals(52, result.posList.get(2).startIndex);
    assertEquals(83, result.posList.get(3).startIndex);

  }

  @Test
  public void test_get_halfsize_numeric_from_japanese_sentence_1() throws Exception {

    // テストの期待動作：独立したフレーズ「300」のみに反応し、「3000」や「30000」に含まれる"300"という文字列に反応しないこと
    final String TEXT = "300を10倍すると3000です。100倍だと30000です。";
    final String SEARCH_PHRASE = "300";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(PhraseAnalysisMode.HANKAKU_NUMERIC_ONLY, result.analysisMode);
    assertEquals(1, result.numOfHits);
    assertEquals("[300]を10倍すると3000です。100倍だと30000です。", result.hint);
    assertEquals(0, result.posList.get(0).startIndex);

  }

  @Test
  public void test_get_halfsize_numeric_from_japanese_sentence_2() throws Exception {

    // テストの期待動作：独立したフレーズ「280」のみに反応し、「MAX280ES」に含まれる"280"という文字列に反応しないこと
    final String TEXT = "新型車両の型番はMAX280ESとなります。280は車両の最高速度からきています。";
    final String SEARCH_PHRASE = "280";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(PhraseAnalysisMode.HANKAKU_NUMERIC_ONLY, result.analysisMode);
    assertEquals(1, result.numOfHits);
    assertEquals("新型車両の型番はMAX280ESとなります。[280]は車両の最高速度からきています。", result.hint);
    assertEquals(22, result.posList.get(0).startIndex);

  }

  @Test
  public void test_get_halfsize_alpha_numeric_from_japanese_sentence_1() throws Exception {

    // テストの期待動作：独立したフレーズ「MAX280ES」のみに反応する。
    final String TEXT = "新型車両の型番はMAX280ESとなります。MAX280ESは車両の最高速度からきています。";
    final String SEARCH_PHRASE = "MAX280ES";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(PhraseAnalysisMode.HANKAKU_ALPHA_NUMERIC, result.analysisMode);
    assertEquals(2, result.numOfHits);
    assertEquals("新型車両の型番は[MAX280ES]となります。[MAX280ES]は車両の最高速度からきています。", result.hint);
    assertEquals(8, result.posList.get(0).startIndex);
    assertEquals(22, result.posList.get(1).startIndex);

  }

  @Test
  public void test_get_halfsize_numeric_from_sentence_1() throws Exception {

    // テストの期待動作：ハイフン("-")は半角英数字の従属要素とみなす（＝半角英数字と連なっているとみなす）ので、このテストでは「280」を検出しない、が正解
    final String TEXT = "MAX-280";
    final String SEARCH_PHRASE = "280";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertFalse(result.isHit);
    assertEquals(PhraseAnalysisMode.HANKAKU_NUMERIC_ONLY, result.analysisMode);
    assertEquals(0, result.numOfHits);
    assertEquals("MAX-280", result.hint);

  }

  @Test
  public void test_get_halfsize_numeric_from_sentence_2() throws Exception {

    // テストの期待動作：アンダースコア("_")は半角英数字の従属要素とみなす（＝半角英数字と連なっているとみなす）ので、このテストでは「280」を検出しない、が正解
    final String TEXT = "MAX_280";
    final String SEARCH_PHRASE = "280";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertFalse(result.isHit);
    assertEquals(0, result.numOfHits);
    assertEquals("MAX_280", result.hint);

  }

  @Test
  public void test_get_fullwidth_katakana_phrase_from_japanese_sentence() throws Exception {

    // テストの期待動作：独立したフレーズ「スキー」のみに反応し、「スキージャンプ」や「ハスキー」に含まれる"スキー"という文字列に反応しないこと
    final String TEXT = "冬といえばスキーが楽しいシーズン。オリンピックのスキージャンプ女子の結果も気になります。ペットでいうとハスキーという犬種も冬が大好き。";
    final String SEARCH_PHRASE = "スキー";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(PhraseAnalysisMode.ZENKAKU_KATAKANA, result.analysisMode);
    assertEquals(1, result.numOfHits);
    assertEquals("冬といえば[スキー]が楽しいシーズン。オリンピックのスキージャンプ女子の結果も気になります。ペットでいうとハスキーという犬種も冬が大好き。", result.hint);
    assertEquals(5, result.posList.get(0).startIndex);
  }

  @Test
  public void test_get_fullwidth_hiragana_phrase_from_japanese_sentence() throws Exception {

    // テストの期待動作：独立したフレーズ「あたご」のみに反応する
    final String TEXT = "日本を代表するイージス艦といえば「戦艦あたご」と「戦艦こんごう」です。あたごに";
    final String SEARCH_PHRASE = "あたご";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(PhraseAnalysisMode.ZENKAKU_HIRAGANA, result.analysisMode);
    assertEquals(1, result.numOfHits);
    assertEquals("日本を代表するイージス艦といえば「戦艦[あたご]」と「戦艦こんごう」です。あたごに", result.hint);
    assertEquals(19, result.posList.get(0).startIndex);
  }

  @Test
  public void test_get_fullwidth_kanji_phrase_from_japanese_sentence() throws Exception {

    // テストの期待動作：独立したフレーズ「日本」のみに反応し、「日本代表」や「日本人」に含まれる"日本"という文字列に反応しないこと
    final String TEXT = "冬のオリンピックに日本からはスノーボードの日本代表として３人の日本人が出場します。日本を応援しましょう。";
    final String SEARCH_PHRASE = "日本";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);
    assertEquals(PhraseAnalysisMode.ZENKAKU_KANJI, result.analysisMode);
    assertTrue(result.isHit);
    assertEquals(2, result.numOfHits);
    assertEquals("冬のオリンピックに[日本]からはスノーボードの日本代表として３人の日本人が出場します。[日本]を応援しましょう。", result.hint);
    assertEquals(9, result.posList.get(0).startIndex);
    assertEquals(41, result.posList.get(1).startIndex);
  }

  @Test
  public void test_get_fullwidth_numeric_phrase_from_japanese_sentence() throws Exception {

    // テストの期待動作：独立したフレーズ「日本」のみに反応し、「日本代表」や「日本人」に含まれる"日本"という文字列に反応しないこと
    final String TEXT = "さて問題です３０を１０倍すると。３００。３００を１０倍すると３０００となります。";
    final String SEARCH_PHRASE = "３０";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertEquals(PhraseAnalysisMode.ZENKAKU_NUMERIC_ONLY, result.analysisMode);
    assertTrue(result.isHit);
    assertEquals(1, result.numOfHits);
    assertEquals("さて問題です[３０]を１０倍すると。３００。３００を１０倍すると３０００となります。", result.hint);
    assertEquals(6, result.posList.get(0).startIndex);

  }

  @Test
  public void test_japanese_findPhrase_for_one_word() throws Exception {

    final String TEXT = "DENT";
    final String SEARCH_PHRASE = "DENT";

    final PhrasefResult result = obj.findPhrase(TEXT, SEARCH_PHRASE);

    assertTrue(result.isHit);
    assertEquals(1, result.numOfHits);
    assertEquals(0, result.posList.get(0).startIndex);

  }

  @Test
  public void test_findPhrases() throws Exception {
    // テストの期待動作：同時に複数のフレーズ検索を実施できるfindPhrasesメソッドを実施する
    final String TEXT = "これから仮想通貨として期待できるのはビットコインよりもむしろビットコインキャッシュであろう。ただ、基盤としてビットコインが消えることは無い。";

    final List<String> SEARCH_PHRASES = new ArrayList<>(Arrays.asList("ビットコイン", "ビットコインキャッシュ"));

    PhrasefResultSet resultSet = obj.findPhrases(TEXT, SEARCH_PHRASES);

    assertTrue(resultSet.isHit);
    assertEquals(3, resultSet.numOfHits);
    assertEquals("これから仮想通貨として期待できるのは[ビットコイン]よりもむしろ[ビットコインキャッシュ]であろう。ただ、基盤として[ビットコイン]が消えることは無い。", resultSet.hint);

    // 個々のフレーズに対するPhraseResultがphraseResultMapに格納される。
    // (キー：フレーズ、値：PhraseResultとなる)
    assertNotNull(resultSet.phraseResultMap);

    // 「ビットコイン」と「ビットコインキャッシュ」の２つのフレーズぶんのphraseResultMapが格納される
    assertEquals(2, resultSet.phraseResultMap.size());

  }

  @Test
  public void test_setPrefix_resetPrefix() throws Exception {
    // テストの期待動作：ヒントでフレーズ部分を示すカッコを変更し、またリセットする
    final String TEXT = "これから仮想通貨として期待できるのはビットコインよりもむしろビットコインキャッシュであろう。ただ、基盤としてビットコインが消えることは無い。";
    final List<String> SEARCH_PHRASES = new ArrayList<>(Arrays.asList("ビットコイン", "ビットコインキャッシュ"));

    obj.setHintBrace("【", "】");
    PhrasefResultSet resultSet1 = obj.findPhrases(TEXT, SEARCH_PHRASES);
    assertEquals("これから仮想通貨として期待できるのは【ビットコイン】よりもむしろ【ビットコインキャッシュ】であろう。ただ、基盤として【ビットコイン】が消えることは無い。", resultSet1.hint);

    obj.resetHintBrace();
    PhrasefResultSet resultSet2 = obj.findPhrases(TEXT, SEARCH_PHRASES);
    assertEquals("これから仮想通貨として期待できるのは[ビットコイン]よりもむしろ[ビットコインキャッシュ]であろう。ただ、基盤として[ビットコイン]が消えることは無い。", resultSet2.hint);

  }
}