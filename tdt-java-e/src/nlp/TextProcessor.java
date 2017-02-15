/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp;

/**
 *
 * @author doried
 */
public class TextProcessor {

    public static String processText(String text) {
        String d = text.toLowerCase();
        String t = "";
        for (int j = 0; j < d.length(); j++) {
            if ((d.charAt(j) >= 'a' && d.charAt(j) <= 'z') || (d.charAt(j) >= 'A' && d.charAt(j) <= 'Z')) {
                t += d.charAt(j);
            } else {
                t += " ";
            }
        }
        t = t.replaceAll(" +", " ");
        String[] words = t.split(" ");
        String res = "";
        for (String word : words) {;
            Stemmer s = new Stemmer();
            s.add(word.toCharArray(), word.toCharArray().length);
            s.stem();
            res += " " + s.toString();
        }

        return res;
    }
}
