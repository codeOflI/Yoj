package com.yoj.utils.verification_code.text.impl;

import com.yoj.utils.verification_code.text.TextProducer;

import java.security.SecureRandom;

/**
 * TextProducer Implementation that will return Chinese characters..
 */
public class ChineseTextProducer implements TextProducer
{
	private String[] simplifiedChineseTexts = new String[]{
			"包括焦点", "新道消点", "服分目搜", "索姓名電", "子郵件信", "主旨請回", "電子郵件", "給我所有", "討論區明", "發表新文", "章此討論", "區所有文", "章回主題",
			"樹瀏覽搜"
	};

	/**
	 * @return random Chinese text
	 */
	public String getText()
	{
		return simplifiedChineseTexts[new SecureRandom().nextInt(simplifiedChineseTexts.length)];
	}
}