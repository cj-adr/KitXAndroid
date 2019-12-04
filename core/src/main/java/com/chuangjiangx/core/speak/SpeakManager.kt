package com.chuangjiangx.core.speak

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.baidu.tts.client.SpeechError
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import com.chuangjiangx.core.KitX
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


object SpeakManager {

    private const val TTS_PARENT_FILENAME = "baiduTTS"
    private const val SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat"
    private const val TEXT_MODEL_NAME = "bd_etts_text.dat"

    private const val ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat"
    private const val ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat"


    private val EXTRA_TEXT = "EXTRA_TEXT"
    private val EXTRA_STOP = "EXTRA_STOP"

    private lateinit var appid: String
    private lateinit var apiKey: String
    private lateinit var apiKeyT: String


    private lateinit var ttsPath: String

    private lateinit var speechSynthesizer: SpeechSynthesizer

    private var errorTry: Int = 0

    private val listener: SpeechSynthesizerListener = object : SpeechSynthesizerListener {
        override fun onSpeechFinish(p0: String?) {
        }

        override fun onSpeechProgressChanged(p0: String?, p1: Int) {
        }

        override fun onSynthesizeFinish(p0: String?) {
        }

        override fun onSpeechStart(p0: String?) {
        }

        override fun onSynthesizeDataArrived(p0: String?, p1: ByteArray?, p2: Int) {
        }

        override fun onError(p0: String?, p1: SpeechError?) {
        }

        override fun onSynthesizeStart(p0: String?) {
        }
    }

    private var inited: Boolean = false


    var initJob: Job? = null

    fun init(appid: String, apiKey: String, apiKeyT: String) {
        if (!inited) {
            this.appid = appid
            this.apiKey = apiKey
            this.apiKeyT = apiKeyT
            initJob = GlobalScope.launch {
                initialEnv(KitX.getContext())
                initialTts(KitX.getContext())
                initJob = null
            }
            inited = true
        }
    }


    fun speak(text: String) {
        GlobalScope.launch {
            initJob?.join()
            try {
                batchSpeak(text, false)
            } catch (ignored: Exception) {
            }
        }
    }


    /**
     * 播报实现
     */
    private fun batchSpeak(text: String, stop: Boolean) {
        if (!TextUtils.isEmpty(text)) {
            if (stop) {
                speechSynthesizer.stop()
            }
            val speak = speechSynthesizer.speak(text)
            if (speak != 0) {
                errorTry++
                if (errorTry <= 1) {
                    batchSpeak(text, stop)
                }
            } else {
                errorTry = 0
            }
        }
    }


    /**
     * 初始化百度语音环境
     */
    private fun initialEnv(context: Context) {
        ttsPath = File(context.filesDir, TTS_PARENT_FILENAME).absolutePath
        makeDir(ttsPath)
        copyFromAssetsToSdcard(context, false, "baidu/$SPEECH_FEMALE_MODEL_NAME"
                , "$ttsPath/$SPEECH_FEMALE_MODEL_NAME")
        copyFromAssetsToSdcard(context, false, "baidu/$TEXT_MODEL_NAME"
                , "$ttsPath/$TEXT_MODEL_NAME")
        copyFromAssetsToSdcard(context, false, "baidu/$ENGLISH_SPEECH_FEMALE_MODEL_NAME"
                , "$ttsPath/$ENGLISH_SPEECH_FEMALE_MODEL_NAME")
        copyFromAssetsToSdcard(context, false, "baidu/$ENGLISH_TEXT_MODEL_NAME"
                , "$ttsPath/$ENGLISH_TEXT_MODEL_NAME")
    }


    private fun makeDir(dirPath: String) {
        val file = File(dirPath)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private fun copyFromAssetsToSdcard(context: Context, isCover: Boolean, source: String, dest: String) {
        val file = File(dest)
        if (isCover || !isCover && !file.exists()) {
            var inputStream: InputStream? = null
            var fos: FileOutputStream? = null
            try {
                inputStream = context.resources.assets.open(source)
                fos = FileOutputStream(dest)
                val buffer = ByteArray(1024)
                var size: Int
                while (true) {
                    size = inputStream!!.read(buffer, 0, 1024)
                    if (size > 0) {
                        fos.write(buffer, 0, size)
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun initialTts(context: Context) {

        speechSynthesizer = SpeechSynthesizer.getInstance()
        speechSynthesizer.setContext(context)
        speechSynthesizer.setSpeechSynthesizerListener(listener)
        // 文本模型文件路径 (离线引擎使用)
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE
                , "$ttsPath/$TEXT_MODEL_NAME")
        // 声学模型文件路径 (离线引擎使用)
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE
                , "$ttsPath/$SPEECH_FEMALE_MODEL_NAME")
        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。
        // this.speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, ttsPath + "/"
        //        + LICENSE_FILE_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)


        speechSynthesizer.setAppId(appid/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/)
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        speechSynthesizer.setApiKey(apiKey, apiKeyT)
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0")
        // 设置Mix模式的合成策略
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK)
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5")
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        val authInfo = speechSynthesizer.auth(TtsMode.MIX)

        if (!authInfo.isSuccess()) {
            val errorMsg = authInfo.ttsError.detailMessage
        }
        // 初始化tts
        try {
            speechSynthesizer.initTts(TtsMode.MIX)
        } catch (ignored: Exception) {
        }

        speechSynthesizer.loadEnglishModel("$ttsPath/$ENGLISH_TEXT_MODEL_NAME"
                , "$ttsPath/$ENGLISH_SPEECH_FEMALE_MODEL_NAME")

    }
}