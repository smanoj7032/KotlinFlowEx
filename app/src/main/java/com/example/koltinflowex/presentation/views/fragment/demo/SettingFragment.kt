package com.example.koltinflowex.presentation.views.fragment.demo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.koltinflowex.R
import com.example.koltinflowex.databinding.SettingFragmentBinding
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.util.MimeTypes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<SettingFragmentBinding>(), View.OnClickListener {
    private val viewModel: SettingViewModel by viewModels()
    private var player: SimpleExoPlayer? = null


    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        val mediaUrl = arguments?.getString("url")
        setUpPlayer()
        if (mediaUrl != null) {
            addMediaItem(mediaUrl)
        }
    }

    private fun addMediaItem(mediaUrl: String) {
        val mediaItem =
            MediaItem.Builder().setUri(mediaUrl).setMimeType(MimeTypes.APPLICATION_M3U8).build()
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.seekTo(0)
        player?.repeatMode = Player.REPEAT_MODE_ONE
        player?.play()
    }

    private fun setUpPlayer() {
        //initializing exoplayer
        player = SimpleExoPlayer.Builder(requireContext()).build()
        //set up audio attributes
        val audioAttributes =
            AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.CONTENT_TYPE_MOVIE)
                .build()
        player?.setAudioAttributes(audioAttributes, false)
        mainbinding.player.player = player
        mainbinding.player.controllerShowTimeoutMs = 0
        mainbinding.player.controllerAutoShow = true
        mainbinding.player.controllerHideOnTouch = false
        //setting the scaling mode to scale to fit
        player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
    }

    companion object {
        fun newInstance(url: String) = SettingFragment().apply {
            arguments = Bundle().apply {
                putString("url", url)
            }
        }
    }

    override fun setObserver() {
    }

    override fun initViews() {
        mainbinding.player.setOnClickListener(this)
    }

    override fun getLayoutResource(): Int {
        return R.layout.setting_fragment
    }

    override fun onResume() {
        super.onResume()
        mainbinding.player.player?.play()
    }

    override fun onPause() {
        super.onPause()
        mainbinding.player.player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainbinding.player.player?.stop()
        mainbinding.player.player?.release()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.player -> {
                if (player?.isPlaying == true) player?.pause() else player?.play()
            }
        }
    }

}