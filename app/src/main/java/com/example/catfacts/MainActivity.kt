package com.example.catfacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catfacts.viewModel.MainViewModel
import com.example.catfacts.viewModel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = MainViewModelFactory(this)
        setContent {
            FactViewer(mainViewModel = viewModel(factory = factory))
        }
    }
}

@Composable
fun FactViewer(mainViewModel: MainViewModel = viewModel()) {
    Column {
        Text(text = mainViewModel.currentFact.value?.fact ?: "Loading...")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { mainViewModel.showNextFact() }) {
            Text("Next Fact")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { mainViewModel.showPreviousFact() }) {
            Text("Previous Fact")
        }
    }
}
