package com.example.composearticle

import android.media.Image
import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composearticle.ui.theme.ComposeArticleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeArticleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeArticle()
                }
            }
        }
    }
}

@Composable
fun ComposeArticle(){
    ComposeArticleApp(
        title = stringResource(R.string.title),
        short_desc = stringResource(R.string.short_desc),
        long_desc = stringResource(R.string.long_desc),
        image = painterResource(R.drawable.bg_compose_background)
    )
}

@Composable
fun ComposeArticleApp(modifier: Modifier = Modifier, image: Painter, long_desc : String, short_desc : String, title : String){
    Column(
        modifier = modifier
    ){
        Image(
            painter = image,
            contentDescription = null
        )

        Text(
            text = title,
            fontSize = 24.sp,
            modifier = modifier.padding(16.dp)

        )

        Text(
            text = short_desc,
            textAlign = TextAlign.Justify,
            modifier = modifier.padding(start = 16.dp, end = 16.dp)
        )

        Text(
            text = long_desc,
            textAlign = TextAlign.Justify,
            modifier = modifier.padding(16.dp)
        )

    }


}

@Preview(showBackground = true)
@Composable
fun ComposeArticlePreview(){
    ComposeArticle()
}