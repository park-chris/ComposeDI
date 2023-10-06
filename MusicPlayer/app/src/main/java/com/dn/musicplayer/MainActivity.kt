

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = Firebase.auth

        binding.kakaoLoginButton.setOnClickListener {
            kakaoLogin()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun kakaoLogin() {

            // 카카오톡 어플이 있으면 카톡으로 로그인, 없으면 카카오 계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    loginWithKaKaoAccount(this)
                } else if (token != null) {
                    getCustomToken(token.accessToken)
                }
            }
        } else {
            loginWithKaKaoAccount(this)
        }
    }

    // 카카오 계정으로 로그인
    private fun loginWithKaKaoAccount(context: Context) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token: OAuthToken?, error: Throwable? ->
            if (token != null) {
                getCustomToken(token.accessToken)
            }
        }
    }

    // firebase functions에 배포한 kakaoCustomAuth 호출
    private fun getCustomToken(accessToken: String) {

        val functions: FirebaseFunctions = Firebase.functions("asia-northeast3")

        val data = hashMapOf(
            "token" to accessToken
        )

        functions
            .getHttpsCallable("kakaoCustomAuth")
            .call(data)
            .addOnCompleteListener { task ->
                try {
                    // 호출 성공
                    val result = task.result?.data as HashMap<*, *>
                    var mKey: String? = null
                    for (key in result.keys) {
                        mKey = key.toString()
                    }
                    val customToken = result[mKey!!].toString()

                    // 호출 성공해서 반환받은 커스텀 토큰으로 Firebase Authentication 인증받기
                    firebaseAuthWithKakao(customToken)
                } catch (e: RuntimeExecutionException) {
                    // 호출 실패
                }
            }
    }

    private fun firebaseAuthWithKakao(customToken: String) {
        auth.signInWithCustomToken(customToken).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                // Firebase Authentication 인증 성공 후 로직
            } else {
                // 실패 후 로직
            }
        }
    }

}