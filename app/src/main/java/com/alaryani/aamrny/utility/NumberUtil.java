package com.alaryani.aamrny.utility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberUtil {

	public static String getNumberFomatDistance(float distance,DecimalFormatSymbols otherSymbols) {


		DecimalFormat df = new DecimalFormat("0.###",otherSymbols);
		return df.format(distance);
	}
}
