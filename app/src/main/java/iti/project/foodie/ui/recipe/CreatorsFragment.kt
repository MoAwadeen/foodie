
package iti.project.foodie.ui.recipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import iti.project.foodie.R
import iti.project.foodie.databinding.FragmentCreatorsBinding

class CreatorsFragment : Fragment() {
    private var _binding: FragmentCreatorsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatorsBinding.inflate(inflater, container, false)
        binding.KaramIn.setOnClickListener {openLinkedInProfile("https://www.linkedin.com/in/karamiibrahim?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app") }
        binding.AzizaIn.setOnClickListener{openLinkedInProfile("https://www.linkedin.com/in/aziza-helmy?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app")}
        binding.AhmedIn.setOnClickListener{openLinkedInProfile("https://www.linkedin.com/in/ahmed-elzamany-621649228")}
        binding.AwadeenIn.setOnClickListener{openLinkedInProfile("https://www.linkedin.com/in/mohamed-awadeen?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app")}
        binding.MernaIn.setOnClickListener { openLinkedInProfile("https://www.linkedin.com/in/merna-hesham-8a94b92b5?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app") }
        binding.HagerIn.setOnClickListener { openLinkedInProfile("https://www.linkedin.com/in/hager-matter-a7301a271?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app") }
        return binding.root


    }
    private fun openLinkedInProfile(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}