package com.example.firststep


import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.firststep.ml.AlphaDetector
import com.example.firststep.ml.DigitDetector
import com.example.firststep.ml.ShapeDetector
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class Classify() {
    private lateinit var output:String;
    private lateinit var actualName:String
    private lateinit var bmp:Bitmap;
    private lateinit var context:Context;
    private var confidence: Float = 0.0f;

    constructor(bmp: Bitmap,context: Context,category: String) : this() {
        this.bmp = bmp
        this.context = context

        when(category){
            "shape"->evaluateShape()
            "digit"->evaluateDigit()
            "alphabet"->evaluateAlpha()
        }
    }


    fun getActualName():String{
        return actualName
    }


    fun getConfidence(): Float{
        Log.d("confidence level",""+confidence)
        return confidence
    }


    fun getType(): String {
        return output;
    }


    /*fun save(bmp: Bitmap){
        // opening a OutputStream to write into the file
        var imageOutStream: OutputStream? = null

        var cv: ContentValues = ContentValues();

        // name of the file
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "H.png");

        // type of the file
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        // location of the file to be saved
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        // get the Uri of the file which is to be created in the storage
        var uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        try {
            // open the output stream with the above uri
            imageOutStream = uri?.let { context.contentResolver.openOutputStream(it) };

            // this method writes the files in storage
            bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);

            // close the output stream after use
            imageOutStream?.close();
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }*/



    private fun evaluateDigit(){
        val model = DigitDetector.newInstance(context)

        // image preprocessing
        val imgProcessor = ImageProcessor.Builder()
            .add(ResizeOp(162,100,ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bmp)
        tensorImage = imgProcessor.process(tensorImage)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 162, 100, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // extract output from result
        var res: FloatArray = outputFeature0.floatArray

        var idx = 0
        for (i in 0..9){
            Log.d("$i","${res[i]*100.0f}%")
            idx = if (res[i] > res[idx]) i else idx
        }

        // set output
        var digit: Array<String> = arrayOf("0","1","2","3","4","5","6","7","8","9")
        output = digit[idx]
        confidence = res[idx]*100.0f

        when(output){
            "0"->{actualName = "zero"}
            "1"->{actualName = "one"}
            "2"->{actualName = "two"}
            "3"->{actualName = "three"}
            "4"->{actualName = "four"}
            "5"->{actualName = "five"}
            "6"->{actualName = "six"}
            "7"->{actualName = "seven"}
            "8"->{actualName = "eight"}
            "9"->{actualName = "nine"}
        }

        // Releases model resources if no longer used.
        model.close()
    }



    private fun evaluateAlpha(){
        val model = AlphaDetector.newInstance(context)

        // image preprocessing
        val imgProcessor = ImageProcessor.Builder()
            .add(ResizeOp(162,100,ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bmp)
        tensorImage = imgProcessor.process(tensorImage)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 162, 100, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // extract output from result
        var res: FloatArray = outputFeature0.floatArray

        var idx = 0
        for (i in 0..25){
            Log.d("$i","${res[i]*100.0f}%")
            idx = if (res[i] > res[idx]) i else idx
        }

        // set output
        var alphabet: Array<String> = arrayOf("A","B","C","D","E","F","G","H","I","J","K","L",
            "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
        output = alphabet[idx]
        confidence = res[idx]*100.0f

        when(output){
            "A"->{actualName = "Letter A"}
            "B"->{actualName = "Letter B"}
            "C"->{actualName = "Letter C"}
            "D"->{actualName = "Letter D"}
            "E"->{actualName = "Letter E"}
            "F"->{actualName = "Letter F"}
            "G"->{actualName = "Letter G"}
            "H"->{actualName = "Letter H"}
            "I"->{actualName = "Letter I"}
            "J"->{actualName = "Letter J"}
            "K"->{actualName = "Letter K"}
            "L"->{actualName = "Letter L"}
            "M"->{actualName = "Letter M"}
            "N"->{actualName = "Letter N"}
            "O"->{actualName = "Letter O"}
            "P"->{actualName = "Letter P"}
            "Q"->{actualName = "Letter Q"}
            "R"->{actualName = "Letter R"}
            "S"->{actualName = "Letter S"}
            "T"->{actualName = "Letter T"}
            "U"->{actualName = "Letter U"}
            "V"->{actualName = "Letter V"}
            "W"->{actualName = "Letter W"}
            "X"->{actualName = "Letter X"}
            "Y"->{actualName = "Letter Y"}
            "Z"->{actualName = "Letter Z"}
        }

        // Releases model resources if no longer used.
        model.close()
    }



    private fun evaluateShape(){
        val model = ShapeDetector.newInstance(context)

        // image preprocessing
        val imgProcessor = ImageProcessor.Builder()
            .add(ResizeOp(162,100,ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bmp)
        tensorImage = imgProcessor.process(tensorImage)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 162, 100, 3), DataType.FLOAT32)
        Log.d("input",inputFeature0.buffer.toString())
        Log.d("loaded",tensorImage.buffer.toString())
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // extract output from result
        var res: FloatArray = outputFeature0.floatArray

        var idx = 0
        for (i in 0..4){
            Log.d("$i","${res[i]*100.0f}%")
            idx = if (res[i] > res[idx]) i else idx
        }

        // set output
        var shape: Array<String> = arrayOf("C","T","R","S","H")
        output = shape[idx]
        confidence = res[idx]*100.0f

        when(output){
            "C"->{actualName = "circle"}
            "T"->{actualName = "triangle"}
            "R"->{actualName = "rectangle"}
            "S"->{actualName = "star"}
            "H"->{actualName = "heart"}
        }

        // Releases model resources if no longer used.
        model.close()

    }
}