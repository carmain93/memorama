package mx.edu.utch.mdapp





import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.edu.utch.mdapp.databinding.ActivityMainBinding
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private var clicked: Boolean = true
    private var turno: Boolean = true
    private var gameFinished: Boolean = false


    private var first_card: ImageView? = null
    private var first_image: Int? = null

    private var cont: Int =0
    private var score1: Int = 0
    private var score2: Int = 0
    private lateinit var fab: FloatingActionButton



    private var deck = ArrayList<Int>(
        listOf(
            R.drawable.cloud,
            R.drawable.day,
            R.drawable.moon,
            R.drawable.night,
            R.drawable.rain,
            R.drawable.rainbow,
            R.drawable.cloud,
            R.drawable.day,
            R.drawable.moon,
            R.drawable.night,
            R.drawable.rain,
            R.drawable.rainbow
        )
    )

    private var images: ArrayList<ImageView>? = null
    private var binding: ActivityMainBinding? = null
    private var scoreViewPlayer1: TextView? = null
    private var scoreViewPlayer2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        images = ArrayList<ImageView>(
            listOf(

                binding!!.gameZone.im11,
                binding!!.gameZone.im12,
                binding!!.gameZone.im13,
                binding!!.gameZone.im21,
                binding!!.gameZone.im22,
                binding!!.gameZone.im23,
                binding!!.gameZone.im31,
                binding!!.gameZone.im32,
                binding!!.gameZone.im33,
                binding!!.gameZone.im41,
                binding!!.gameZone.im42,
                binding!!.gameZone.im43
            )
        )

        scoreViewPlayer1 = binding!!.scoreZone.mainActivityTvPlayer1
        scoreViewPlayer2 = binding!!.scoreZone.mainActivityTvPlayer2

        fab = findViewById(R.id.fabPrincipal)
        //inicio del boton de
        fab.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("¡juego teminado!")
            alertDialogBuilder.setMessage("jufador uno : ${score1}, jugador dos :$score2")
            alertDialogBuilder.setPositiveButton("Reiniciar"){
                    _, _ ->
                resetGame()
            }
            alertDialogBuilder.setNegativeButton("Salir") { dialog, which ->
                finish()
            }
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.show()
        }
        Collections.shuffle(deck)
        startOn()
        clickOn()

        setSupportActionBar(binding!!.mainBottomAppBar)
    }

    private fun startOn() {
        if (turno) {
            binding!!.scoreZone.mainActivityTvPlayer1.setBackgroundColor(Color.GREEN)
            binding!!.scoreZone.mainActivityTvPlayer2.setBackgroundColor(Color.TRANSPARENT)
        } else {
            binding!!.scoreZone.mainActivityTvPlayer1.setBackgroundColor(Color.TRANSPARENT)
            binding!!.scoreZone.mainActivityTvPlayer2.setBackgroundColor(Color.GREEN)
        }
        binding!!.scoreZone.mainActivityTvPlayer1.setTypeface(null, Typeface.BOLD_ITALIC)
        binding!!.scoreZone.mainActivityTvPlayer2.setTypeface(null, Typeface.BOLD_ITALIC)
    }

    private fun clickOn() {
        for (i in images!!.indices) {
            images!![i]!!.setOnClickListener {
                images!![i]!!.setImageResource(deck[i])
                saveClick(images!![i]!!, deck[i])
            }
        }
    }
    ///
    private fun saveClick(img: ImageView, card: Int) {
        if (clicked) {
            first_card = img
            first_image = card
            first_card!!.isEnabled = false
            clicked = !clicked
        } else {
            xtivate(false)
            var handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (card == first_image) {
                    first_card!!.isVisible = false
                    img.isVisible = false
                    if (turno) {
                        score1++
                        cont++

                        if (cont ==6 && score1==score2){showWinnerDialog("ubo un empate que bien")}
                        else if (cont==6){showWinnerDialog("el jugador uno es el ganador")}
                    } else {
                        score2++
                        cont++

                        if (cont ==6 && score1==score2){showWinnerDialog("ubo un empate que bien")}
                        else if (cont==6){showWinnerDialog("el jugador dos es el ganador")}
                    }
                    startOn()
                    xtivate(true)
                } else {
                    first_card!!.setImageResource(R.drawable.reverso)
                    img.setImageResource(R.drawable.reverso)
                    first_card!!.isEnabled = true
                    turno = !turno
                    startOn()
                    xtivate(true)
                }
                updateScores()
                checkGameFinished()
            }, 1000)//se altero de 2000 para poder checar mas rapido
            clicked = !clicked
        }

    }
    ///
    private fun xtivate(b: Boolean) {
        for (i in images!!.indices) {
            images!![i]!!.isEnabled = b
        }
    }

    private fun updateScores() {
        scoreViewPlayer1?.text = "Jugador 1: $score1"
        scoreViewPlayer2?.text = "Jugador 2: $score2"
    }

    private fun checkGameFinished() {
        val allMatched = deck.none { it != 0 }
        if (allMatched && !gameFinished) {
            gameFinished = true
            showGameResultDialog()
        }
    }

    private fun showGameResultDialog() {
        val winner = if (score1 > score2) "Jugador 1" else if (score1 < score2) "Jugador 2" else "Empate"
        val message = if (winner == "Empate") "¡Es un empate!" else "¡$winner gana!"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Fin del juego!")
            .setMessage(message)
            .setPositiveButton("Reiniciar") { _, _ ->
                resetGame()
            }
            .setNegativeButton("Salir") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetGame() {
        for (i in (0..<images!!.size)){
            images!![i]!!.isVisible = true
            images!![i]!!.setImageResource(R.drawable.reverso)
        }
        clickOn()
        xtivate(true)
        clicked = true
        turno = true
        cont = 0
        score1 = 0
        score2 = 0
        updateScores()
        startOn()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_1 -> {
                resetGame()
                true
            }
            R.id.option_2 -> {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("¡juego teminado!")
                alertDialogBuilder.setMessage("jufador uno : ${score1}, jugador dos :$score2")
                alertDialogBuilder.setNegativeButton("Salir") { dialog, which ->
                    finish()
                }
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.show()
                true
            }
            R.id.option_3 -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHelp() {

    }
    //funcion para el final
    private fun showWinnerDialog(winner: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("¡$winner gana!")
        alertDialogBuilder.setMessage("¿Qué quieres hacer a continuación?")
        alertDialogBuilder.setPositiveButton("Reiniciar") { dialog, which ->
            resetGame()
        }
        alertDialogBuilder.setNegativeButton("Salir") { dialog, which ->
            finish()
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)

        return true
    }


}