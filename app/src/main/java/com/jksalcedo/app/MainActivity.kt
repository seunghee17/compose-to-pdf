package com.jksalcedo.app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jksalcedo.app.ui.theme.ComposeToPDFTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val content = rememberTextFieldState()
            Layout(this, content)
        }

    }
}

@Composable
fun Layout(context: Context, content: TextFieldState) {
    ComposeToPDFTheme {
        val scope = rememberCoroutineScope()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    state = content,
                    label = { Text("Content to write") },
                    modifier = Modifier.padding(innerPadding)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    if (content.text.isNotEmpty()) {
                        scope.launch(Dispatchers.IO) {
                            val pdfGenerator = PdfGenerator(context = context)
                            val path = context.getExternalFilesDir("PDF")

                            path?.let {
                                val file = File(it, "test.pdf")
                                val outputStream = FileOutputStream(file)
                                val result = pdfGenerator.generateLongContent(
                                    outputStream = outputStream,
                                    pageSize = PdfPageSize.A4(72)
                                        .orientation(Orientation.PORTRAIT),
                                    margin = 160.dp,
                                    content = { Greeting() }
                                )
                                withContext(Dispatchers.Main) {
                                    if (result.isSuccess) {
                                        Toast.makeText(
                                            context,
                                            "PDF generated at ${file.absolutePath}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "PDF generation failed!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Content cannot be empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }) {
                    Text("Generate PDF")
                }

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
    ComposeToPDFTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    state = TextFieldState(),
                    label = { Text("Content to write") },
                    modifier = Modifier.padding(innerPadding)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                }) {
                    Text("Generate PDF")
                }
            }

        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DebugConfigStamp() {
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val window = LocalWindowInfo.current

    Column(
        modifier = Modifier
            .border(1.dp, Color.Red)
            .padding(10.dp)
    ) {
        Text("DEBUG REPORT:")
        Text("Density: ${density.density}")
        Text("Font Scale: ${density.fontScale}")
        Text("Config Screen Width: ${config.screenWidthDp}")
        Text("Window Screen Width: ${window.containerSize.width}")
    }
}

@Composable
fun Greeting() {
    Column {
        Text(
            modifier = Modifier.padding(all = 30.dp),
            text = "Hello 월드여!!!",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
        )
        Text(
            text = "Hello 월드여!!! Bold 입니다!",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            text = "해 질 녘, 붉게 물든 노을이 서쪽 하늘을 가득 채우며 잔잔한 바다 위에 긴 황금빛 띠를 만들어내고, 갈매기 한 마리가 외로이 수평선을 향해 날아가며 내뿜는 날갯짓은 마치 잊혀진 추억의 파편처럼 덧없으면서도 눈부시게 아름다웠다.\"",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Button(
            onClick = {

            },
            modifier = Modifier
                .size(width = 200.dp, height = 50.dp),
            content = { Text(text = "클릭") }
        )
        Text(
            modifier = Modifier.padding(all = 30.dp),
            text = "Hello 월드여!!!",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
        )
        Text(
            text = "Hello 월드여!!! Bold 입니다!",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            text = "해 질 녘, 붉게 물든 노을이 서쪽 하늘을 가득 채우며 잔잔한 바다 위에 긴 황금빛 띠를 만들어내고, 갈매기 한 마리가 외로이 수평선을 향해 날아가며 내뿜는 날갯짓은 마치 잊혀진 추억의 파편처럼 덧없으면서도 눈부시게 아름다웠다.\"",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            modifier = Modifier.padding(all = 30.dp),
            text = "Hello 월드여!!!",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
        )
        Text(
            text = "Hello 월드여!!! Bold 입니다!",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            text = "해 질 녘, 붉게 물든 노을이 서쪽 하늘을 가득 채우며 잔잔한 바다 위에 긴 황금빛 띠를 만들어내고, 갈매기 한 마리가 외로이 수평선을 향해 날아가며 내뿜는 날갯짓은 마치 잊혀진 추억의 파편처럼 덧없으면서도 눈부시게 아름다웠다.\"",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.user),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(all = 30.dp),
            text = "Hello 월드여!!!",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
        )
        Text(
            text = "Hello 월드여!!! Bold 입니다!",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            text = "해 질 녘, 붉게 물든 노을이 서쪽 하늘을 가득 채우며 잔잔한 바다 위에 긴 황금빛 띠를 만들어내고, 갈매기 한 마리가 외로이 수평선을 향해 날아가며 내뿜는 날갯짓은 마치 잊혀진 추억의 파편처럼 덧없으면서도 눈부시게 아름다웠다.\"",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            modifier = Modifier.padding(all = 30.dp),
            text = "Hello 월드여!!!",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
        )
        Text(
            text = "Hello 월드여!!! Bold 입니다!",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            text = "해 질 녘, 붉게 물든 노을이 서쪽 하늘을 가득 채우며 잔잔한 바다 위에 긴 황금빛 띠를 만들어내고, 갈매기 한 마리가 외로이 수평선을 향해 날아가며 내뿜는 날갯짓은 마치 잊혀진 추억의 파편처럼 덧없으면서도 눈부시게 아름다웠다.\"",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            modifier = Modifier.padding(all = 30.dp),
            text = "Hello 월드여!!!",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
        )
        Text(
            text = "Hello 월드여!!! Bold 입니다!",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            text = "해 질 녘, 붉게 물든 노을이 서쪽 하늘을 가득 채우며 잔잔한 바다 위에 긴 황금빛 띠를 만들어내고, 갈매기 한 마리가 외로이 수평선을 향해 날아가며 내뿜는 날갯짓은 마치 잊혀진 추억의 파편처럼 덧없으면서도 눈부시게 아름다웠다.\"",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            modifier = Modifier.padding(all = 30.dp),
            text = "Hello 월드여!!!",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
        )
        Text(
            text = "Hello 월드여!!! Bold 입니다!",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            text = "해 질 녘, 붉게 물든 노을이 서쪽 하늘을 가득 채우며 잔잔한 바다 위에 긴 황금빛 띠를 만들어내고, 갈매기 한 마리가 외로이 수평선을 향해 날아가며 내뿜는 날갯짓은 마치 잊혀진 추억의 파편처럼 덧없으면서도 눈부시게 아름다웠다.\"",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            modifier = Modifier.padding(all = 30.dp),
            text = "Hello 월드여!!!",
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
        )
        Text(
            text = "Hello 월드여!!! Bold 입니다!",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        Text(
            text = "해 질 녘, 붉게 물든 노을이 서쪽 하늘을 가득 채우며 잔잔한 바다 위에 긴 황금빛 띠를 만들어내고, 갈매기 한 마리가 외로이 수평선을 향해 날아가며 내뿜는 날갯짓은 마치 잊혀진 추억의 파편처럼 덧없으면서도 눈부시게 아름다웠다.\"",
            modifier = Modifier.padding(all = 30.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        )
        PdfAsyncImage(
            model = "https://www.pixelstalk.net/wp-content/uploads/2016/06/HD-images-of-nature-download.jpg",
            contentDescription = ""
        )
        PdfAsyncImage(
            model = "https://www.pixelstalk.net/wp-content/uploads/2016/06/HD-images-of-nature-download.jpg",
            contentDescription = ""
        )
        PdfAsyncImage(
            model = "https://www.pixelstalk.net/wp-content/uploads/2016/06/HD-images-of-nature-download.jpg",
            contentDescription = ""
        )
    }

}