package com.jetpackcomposedemp

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.core.sp
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.surface.Surface
import androidx.ui.res.imageResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FlexColumn {
                    inflexible {
                        TopAppBar(title = {
                            Text("Jetpack Compose Demo")
                        })
                    }
                    flexible(flex = 1f) {
                        RecipeList(defaultRecipes)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Column {
        Text(text = "Hello World!", style = TextStyle(color = Color.Red))
        Text(text = "Hello Second World!", style = TextStyle(color = Color.Red))
        Text(text = "Hello Third World!", style = TextStyle(color = Color.Red))
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Surface(shape = RoundedCornerShape(8.dp), elevation = 8.dp) {
        val image = +imageResource(recipe.imageResource)

        Column(modifier = Spacing(0.dp)) {

            Container(expanded = true, height = 144.dp) {
                DrawImage(image = image)
            }

            Padding(top = 5.dp, left = 5.dp, right = 5.dp, bottom = 5.dp) {
                Text(recipe.title, style = TextStyle(color = Color.Blue, fontSize = 25.sp))
            }

            for (ingredient in recipe.ingredients) {
                Padding(top = 5.dp, left = 5.dp, right = 5.dp, bottom = 5.dp) {
                    Text("• $ingredient",  style = TextStyle(fontSize = 15.sp))
                }
            }
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>) {
    VerticalScroller {
        Column {
            for (recipe in recipes) {
                Padding(16.dp) {
                    RecipeCard(recipe)
                }
            }
        }
    }
}

@Composable
@Preview
fun DefaultRecipeCard() {
    MaterialTheme {
        RecipeCard(defaultRecipes[0])
    }
}