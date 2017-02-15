/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

import tdt.Word;

/**
 *
 * @author doried
 */
public interface IIDFProvider {
	int getDocumentFrequency(Word w);
    double getInverseDocumentFrequency(Word w);
    double getAverageDocumentLength();
    int getTotalDocumentsCount();
}
