package com.example.project.coordinator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.showAlignTop

class MarkEndrosementActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_endrosement)

        val colorStateListRed = ContextCompat.getColorStateList(this, R.color.deep_red)
        val colorStateListYellow = ContextCompat.getColorStateList(this, R.color.deep_yellow)
        val colorStateListGreen = ContextCompat.getColorStateList(this, R.color.deep_green)

        val submissionId = intent.getStringExtra("submissionId")
        val UserId = intent.getStringExtra("UserId")

        val tvStudentName = findViewById<TextView>(R.id.student_name3)
        val scorebar = findViewById<TextView>(R.id.scorebar)
        var tvTitle = findViewById<TextView>(R.id.title)
        val btnApprove = findViewById<Button>(R.id.btn_approve)


        val proposal = findViewById<TextView>(R.id.proposal)

        val rekabentuk = findViewById<TextView>(R.id.rekabentuk)
        val konfigurasi = findViewById<TextView>(R.id.konfigurasi)
        val metodologi1 = findViewById<TextView>(R.id.metodologi1)

        val abstrak = findViewById<TextView>(R.id.abstrak)
        val pendahuluan = findViewById<TextView>(R.id.pendahuluan)
        val literatur = findViewById<TextView>(R.id.literatur)
        val metodologi2 = findViewById<TextView>(R.id.metodologi2)
        val sistem = findViewById<TextView>(R.id.sistem)
        val perlaksanaan = findViewById<TextView>(R.id.perlaksanaan)
        val perbincangan = findViewById<TextView>(R.id.perbincangan)
        val kesimpulan = findViewById<TextView>(R.id.kesimpulan)
        val rujukan = findViewById<TextView>(R.id.rujukan)
        val penulisan = findViewById<TextView>(R.id.penulisan)

        val userReference = db.collection("users").document(UserId!!)

        userReference.get().addOnSuccessListener { submissionSnapshot ->
            if (submissionSnapshot.exists()){
                val firstName = submissionSnapshot.getString("first_name")
                val lastName = submissionSnapshot.getString("last_name")
                val title = submissionSnapshot.getString("title")

                tvStudentName.text = "$firstName $lastName"
                tvTitle.text = title
            }
        }

        val markReference = db.collection("mark").document(UserId)

        markReference.get().addOnSuccessListener { markSnapshot ->
            if(markSnapshot.exists()){
                val proposalMark = markSnapshot.getLong("proposal")!!.toString()

                val rekabentukMark = markSnapshot.getLong("rekabentuk")!!.toString()
                val konfigurasiMark = markSnapshot.getLong("konfigurasi")!!.toString()
                val metodologi1Mark = markSnapshot.getLong("metodologi1")!!.toString()

                val abstrakMark = markSnapshot.getLong("abstrak")!!.toString()
                val pendahuluanMark = markSnapshot.getLong("pendahuluan")!!.toString()
                val literaturMark = markSnapshot.getLong("literatur")!!.toString()
                val metodologi2Mark = markSnapshot.getLong("metodologi2")!!.toString()
                val sistemMark = markSnapshot.getLong("sistem")!!.toString()
                val perlaksanaanMark = markSnapshot.getLong("perlaksanaan")!!.toString()
                val perbincanganMark = markSnapshot.getLong("perbincangan")!!.toString()
                val kesimpulanMark = markSnapshot.getLong("kesimpulan")!!.toString()
                val rujukanMark = markSnapshot.getLong("rujukan")!!.toString()
                val penulisanMark = markSnapshot.getLong("penulisan")!!.toString()

                val totalMark= markSnapshot.getLong("total_mark")!!.toString()
                val totalMarkInt = markSnapshot.getLong("total_mark")!!.toInt()
                val title = markSnapshot.getString("title")


                if (totalMarkInt in 1..19) {
                    scorebar.backgroundTintList = colorStateListRed
                }else if(totalMarkInt in 20..39){
                    scorebar.backgroundTintList = colorStateListYellow
                }else{
                    scorebar.backgroundTintList = colorStateListGreen
                }

                tvTitle.text = title

                proposal.text = proposalMark

                rekabentuk.text = rekabentukMark
                konfigurasi.text = konfigurasiMark
                metodologi1.text = metodologi1Mark

                abstrak.text = abstrakMark
                pendahuluan.text = pendahuluanMark
                literatur.text = literaturMark
                metodologi2.text = metodologi2Mark
                sistem.text = sistemMark
                perlaksanaan.text = perlaksanaanMark
                perbincangan.text = perbincanganMark
                kesimpulan.text = kesimpulanMark
                rujukan.text = rujukanMark
                penulisan.text = penulisanMark

                scorebar.text = totalMark
            }
        }

        btnApprove.setOnClickListener{

            val submissionStatus = "Approved"

            val data = mapOf(
                "submission_status" to submissionStatus
            )

            val submissionReference = db.collection("submission").document(submissionId!!)

            submissionReference.collection("users").document(UserId)
                .update(data).addOnSuccessListener { usersSnapshot ->
                    // The data was successfully saved
                    Toast.makeText(baseContext, "Endorsement have been approve",
                        Toast.LENGTH_LONG).show()

                    finish()
                }
        }

        fun balloon(tooltipText: String) = createBalloon(baseContext) {
            setArrowSize(10)
//            setWidthRatio(1f)
//            setHeight(250)
            setWidth(BalloonSizeSpec.WRAP) // sets width size depending on the content's size.
            setHeight(BalloonSizeSpec.WRAP) // sets height size depending on the content's size.
            setArrowPosition(0.89f)
            setCornerRadius(8f)
            setArrowAlignAnchorPadding(10)
            setAlpha(0.9f)
            setText(tooltipText)
            setTextGravity(paddingLeft)
            setPadding(15)
            setTextSize(15f)
            setIconSpace(-5)
//            setIconDrawable(ContextCompat.getDrawable(baseContext, R.drawable.warning_sign))
            setIconColor(resources.getColor(R.color.black))
            setTextColorResource(R.color.black)
            setBackgroundColorResource(R.color.tooltipyellow)
            setBalloonAnimation(BalloonAnimation.ELASTIC)
            setLifecycleOwner(lifecycleOwner)

//            setOnBalloonClickListener {
//                startActivity(Intent(this@SubmissionRatingActivity, SubmissionRatingActivity::class.java))
//            }
        }

        val textArray =
            arrayOf(
                "Laporan proposal (P01, C4(analisis)) : 5%  pengetahuan\n" +
                        "\n" +
                        "Perlu mempunyai elemen berikut:\n" +
                        "•\tPengenalan tentang projek dinyatakan dengan jelas  \n" +
                        "•\tPernyataan masalah dinyatakan dengan jelas\n" +
                        "•\tKajian literatur adalah relevan dengan pernyataan masalah\n" +
                        "•\tObjektif yang hendak dicapai selaras dengan pernyataan masalah\n" +
                        "•\tSkop projek bersesuaian dengan tahap prasiswazah \n" +
                        "•\tJangkaan hasil yang boleh dicapai\n" +
                        "•\tPerancangan projek dirangka dengan tersusun",

                "Rekabentuk Projek (CPS9-P4(mekanisme)) - 5%  psikomotor",

                "Konfigurasi persekitaran projek (CPS9-P5(respons ketara kompleks)) – 5%  psikomotor. Pelajar berupaya menunjukkan  kemahiran berikut:\n" +
                        "•\tmelaksanakan sebahagian langkah-langkah pembangunan atau implementasi projek\n" +
                        "•\tmengkonfigurasi atau memanipulasi persekitaran pembangunan dan pelaksanaan projek  \n",

                "Pemilihan metodologi/teknik/perisian (CSP4-CTPS) CT1(kebolehan mengenalpasti dan menganalisis masalah dalam situasi kompleks dan kabur, serta membuat penilaian yang berjustifikasi) 5%",

                "0 - Abstrak tiada dalam tesis." +
                        "\n1 - Abstrak merangkumi kurang daripada tiga elemen berikut iaitu pengenalan, pernyataan masalah, objektif, metodologi, hasil kajian dan kesimpulan DAN tidak ditulis menggunakan bahasa penulisan yang baik dalam Bahasa Melayu atau Bahasa Inggeris." +
                        "\n2 - Abstrak merangkumi sekurang-kurangnya tiga elemen berikut iaitu pengenalan, pernyataan masalah, objektif, metodologi, hasil kajian dan kesimpulan DAN ditulis menggunakan bahasa penulisan yang baik dalam Bahasa Melayu dan Bahasa Inggeris." +
                        "\n3 - Abstrak merangkumi enam elemen berikut iaitu pengenalan, pernyataan masalah, objektif, metodologi, hasil kajian dan kesimpulan yang mengambarkan kandungan keseluruhan projek DAN ditulis menggunakan bahasa penulisan yang baik dalam Bahasa Melayu dan Bahasa Inggeris.",

                "0 - Pendahuluan tiada dalam tesis." +
                        "\n1 - Pendahuluan merangkumi mana-mana satu elemen berikut iaitu pernyataan masalah, objektif, jangkaan hasil projek dan organisasi tesis tetapi tidak berkaitan dengan projek yang dijalankan DAN ditulis menggunakan gaya penulisan yang kurang tersusun." +
                        "\n2 - Pendahuluan merangkumi mana-mana dua elemen berikut iaitu pernyataan masalah, objektif, jangkaan hasil projek dan organisasi tesis yang tidak mengambarkan projek DAN ditulis menggunakan gaya penulisan kurang tersusun." +
                        "\n3 - Pendahuluan merangkumi mana-mana tiga elemen berikut iaitu pernyataan masalah, objektif, jangkaan hasil projek dan organisasi tesis yang mengambarkan projek DAN ditulis menggunakan gaya penulisan tersusun." +
                        "\n4 - Pendahuluan merangkumi semua empat elemen berikut iaitu pernyataan masalah, objektif, jangkaan hasil projek dan organisasi tesis yang mengambarkan projek DAN ditulis menggunakan gaya penulisan tersusun yang menunjukkan kesinambungan antara setiap elemen.",

                "0 - Kajian literatur tiada dalam tesis." +
                        "\n1 - Kajian literatur yang tidak relevan dan tanpa rumusan." +
                        "\n2 - Kajian literatur yang relevan daripada pelbagai sumber dan tanpa rumusan." +
                        "\n3 - Kajian literatur yang relevan daripada pelbagai sumber dan beserta rumusan ringkas." +
                        "\n4 - Kajian literatur yang relevan daripada pelbagai sumber beserta pandangan dan rumusan secara kritis." +
                        "\n5 - Kajian literatur yang relevan dan terkini daripada pelbagai sumber, beserta pandangan dan rumusan secara kritis.",

                "0 - Metodologi tiada dalam tesis." +
                        "\n1 - Pemilihan metodologi yang tidak sesuai dan tidak dihuraikan dengan baik." +
                        "\n2 - Pemilihan metodologi yang sesuai tetapi tidak dihuraikan dengan baik." +
                        "\n3 - Pemilihan metodologi yang sesuai serta dihuraikan dengan baik." +
                        "\n4 - Pemilihan metodologi yang sesuai serta dihuraikan dengan baik dan terperinci." +
                        "\n5 - Pemilihan metodologi yang sesuai serta dihuraikan dengan jelas, terperinci dan tersusun yang menunjukkan kesinambungan antara setiap langkah.",

                "0 - Reka bentuk sistem tiada dalam tesis." +
                        "\n1 - Pemilihan reka bentuk sistem yang tidak sesuai." +
                        "\n2 - Pemilihan reka bentuk sistem yang sesuai tetapi tidak dihuraikan dengan baik." +
                        "\n3 - Pemilihan reka bentuk sistem yang sesuai serta dihuraikan dengan baik." +
                        "\n4 - Pemilihan reka bentuk sistem yang sesuai serta dihuraikan dengan baik, betul dan terperinci." +
                        "\n5 - Pemilihan reka bentuk sistem yang sesuai serta dihuraikan dengan jelas dan terperinci yang digambarkan menggunakan perisian yang bersesuaian seperti CASE tool.",

                "0 - Perlaksanaan tiada dalam tesis." +
                        "\n 1 - Perlaksanaan tidak memenuhi objektif projek atau tidak relevan." +
                        "\n2 - Perlaksanaan memenuhi sebahagian objektif projek." +
                        "\n3 - Perlaksanaan memenuhi sebahagian objektif projek dan dibincangkan dengan ringkas." +
                        "\n4 - Perlaksanaan memenuhi keseluruhan objektif projek dan dibincangkan dengan baik." +
                        "\n5 - Perlaksanaan memenuhi keseluruhan objektif projek dan dibincangkan dengan jelas dan terperinci.",

                "0 - Perbincangan tiada dalam tesis." +
                        "\n1- Perbincangan tidak memenuhi objektif projek atau tidak relevan." +
                        "\n2 - Perbincangan memenuhi sebahagian objektif projek." +
                        "\n3 - Perbincangan memenuhi sebahagian objektif projek dan ditulis dengan ringkas." +
                        "\n4 - Perbincangan memenuhi keseluruhan objektif projek dan ditulis dengan baik." +
                        "\n5 - Perbincangan memenuhi keseluruhan objektif projek dan ditulis dengan jelas dan terperinci.",

                "0 - Kesimpulan dan cadangan tiada dalam tesis." +
                        "\n1 - Kesimpulan atau cadangan dinyatakan dengan ringkas." +
                        "\n2 - Kesimpulan dibincangkan dengan baik dan cadangan dikemukakan dengan baik." +
                        "\n3 - Kesimpulan dibincangkan dengan jelas dan menyeluruh dan cadangan kreatif dikemukakan.",

                "0 - Tiada rujukan dalam tesis." +
                        "\n1 - Terdapat rujukan yang tidak relevan dan tidak mengikut format penulisan rujukan." +
                        "\n2 - Semua rujukan adalah relevan dan mengikut format penulisan rujukan.",

                "0 - Semua kandungan tesis yang merangkumi penggunaan teks, gambar rajah, jadual ataupun lain-lain elemen disalin daripada sumber sedia ada." +
                        "\n1 - Sebahagian besar kandungan tesis yang merangkumi penggunaan teks, gambar rajah, jadual ataupun lain-lain elemen disalin daripada sumber sedia ada tanpa menggunakan kaedah sitasi (citation) yang betul." +
                        "\n2 - Sebahagian besar kandungan tesis yang merangkumi penggunaan teks, gambar rajah, jadual ataupun lain-lain elemen disalin daripada sumber sedia ada dirujuk dengan menggunakan kaedah sitasi (citation) yang betul." +
                        "\n3 - Keseluruhan kandungan tesis yang merangkumi penggunaan teks, gambar rajah, jadual ataupun lain-lain elemen disalin daripada sumber sedia ada dirujuk dengan menggunakan kaedah sitasi (citation) yang betul.")

        for (id in 1..14) {
            val tooltip =
                findViewById<ImageView>(resources.getIdentifier("tooltip$id", "id", packageName))
            tooltip.setOnClickListener {
                tooltip.showAlignTop(balloon(textArray[id-1]))
            }
        }
    }
}