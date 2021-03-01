package com.iflytek.speech.tts;

public interface ITtsListener {
	void onTtsMsgProc(int uMsg, int wParam, String lParam);

	void onProgress(int nTextIndex, int nTextLen);
}
