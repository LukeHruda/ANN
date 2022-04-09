import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.TextStyle;
import java.util.Arrays;

public class ann  
{  
    static int epoch = 1;

    static double[] input = new double[45];
    static double[][] w1 = new double[3][45];
    static double[] middleLayer = {0,0,0};
    static double[][] w2 = new double[3][10];
    static double[] output = {0,0,0,0,0,0,0,0,0,0};
    static double[] error = new double[10];
    static double[] errorGradient = new double[10];
    static double[] middleGradient = new double[3];
    static double[] outputWeightAdjustment = new double[10];
    static double[] outputThreshhold = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    static double[] outputThreshholdAdjustment = new double[10];
    static double sumErrorSq = 0;
    static double[] one = {0,0,1,0,0,0,1,1,0,0,1,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0};
    static double[] two = {0,1,1,1,0,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,1,1,1,1,1};
    static double[] three = {0,1,1,1,0,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,1,1,0};
    static double[] four = {0,0,0,1,0,0,0,1,1,0,0,0,1,1,0,0,1,0,1,0,0,1,0,1,0,1,0,0,1,0,1,1,1,1,1,0,0,0,1,0,0,0,0,1,0};
    static double[] five = {1,1,1,1,1,1,0,0,0,0,1,0,0,0,0,1,1,1,1,0,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,1,1,0};
    static double[] six = {0,1,1,1,0,1,0,0,0,1,1,0,0,0,0,1,0,0,0,0,1,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,0};
    static double[] seven = {1,1,1,1,1,0,0,0,0,1,0,0,0,1,0,0,0,0,1,0,0,0,1,0,0,0,0,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0};
    static double[] eight = {0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,0};
    static double[] nine = {0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,1,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,1,1,0};
    static double[] zero = {0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,0};

    static double[][] trainingData = new double[10][45];
    static double[][] testData = {{0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0},{0,1,1,0,0,1,1,1,0,0,1,1,1,0,0,1,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,1,1,1,1,1},{0,0,1,0,1,0,1,1,0,0,1,0,1,0,1,0,0,1,0,0,0,0,1,1,0,1,0,1,0,0,0,0,1,0,1,0,0,1,0,0,1,1,1,1,1},{0,1,1,1,0,1,1,0,1,1,1,0,0,0,1,0,0,0,0,1,0,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,0,1,1,1,1,1},{0,1,1,1,0,1,0,0,0,1,0,0,1,0,1,0,0,0,0,1,1,0,0,1,0,0,0,1,0,0,0,1,0,0,1,1,0,0,0,0,1,1,1,1,1},{0,1,0,1,0,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,1,1,1,0},{0,1,1,1,0,1,0,0,0,1,0,0,1,0,1,1,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,0,1,0,1,1,0,0,0,1,0,1,1,1,0},{0,1,1,1,0,1,1,0,1,1,1,0,0,0,1,0,0,0,1,1,0,0,1,1,0,0,0,0,1,1,1,0,0,0,1,1,1,0,1,1,0,1,1,1,0},{0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,0},{0,0,0,1,0,0,0,1,1,0,0,0,1,1,0,0,1,1,1,0,0,1,0,1,0,1,0,0,1,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0},{0,1,0,1,0,1,0,1,1,0,0,0,1,1,0,0,1,0,1,0,0,1,0,1,0,1,0,1,1,0,1,1,1,1,1,0,0,0,1,0,0,1,0,1,0},{0,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,1,1,1,1,0,0,0,1,0,0,0,0,1,0},{1,1,1,1,1,1,0,0,0,0,1,1,1,1,0,1,0,0,0,1,0,0,1,0,1,1,0,1,0,1,0,0,1,0,1,1,0,0,0,1,0,1,1,1,0},{1,1,1,1,1,1,0,0,0,0,1,0,0,0,0,1,1,1,1,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,1,1,0},{1,1,1,1,1,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1,1,1,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1,1,1,1,1},{0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,0,1,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,0},{0,1,1,1,0,1,0,0,0,1,1,0,1,0,0,1,0,0,0,0,1,1,1,1,0,1,0,0,0,1,1,0,1,0,1,1,0,0,0,1,0,1,1,1,0},{0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0},{1,1,1,1,1,0,0,0,0,1,0,1,0,1,0,0,0,0,1,0,1,0,1,0,0,0,0,1,0,1,0,1,0,1,0,0,1,0,0,0,0,1,0,0,1},{1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,0,0,1,1,1,0,0,1,1,0,0,1,1,1,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0},{1,1,1,1,1,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,1,1,0,0,0,1,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0},{0,1,1,1,0,1,0,0,0,1,1,0,1,0,1,1,0,0,0,1,0,1,1,1,0,1,0,0,0,1,1,0,1,0,1,1,0,0,0,1,0,1,1,1,0},{1,1,1,1,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,1,1,1,1},{0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,1,1,1,0,1,0,0,0,1,1,0,1,0,1,1,0,0,0,1,0,1,1,1,1,0,0,0,0,1,0,0,1,0,1,1,0,0,0,1,0,1,1,1,0},{0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,0,0,0,0,0,1,0,0,0,0,1,1,0,0,0,1,0,1,1,1,0},{0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0},{0,1,1,1,0,1,0,0,0,1,1,0,1,0,1,1,0,1,0,1,1,0,1,0,1,1,0,1,0,1,1,0,1,0,1,1,0,0,0,1,0,1,1,1,0},{1,1,1,1,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,1,1,1,1},{0,0,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,1,1,0,0,0,0,0,0}};

    static double[] zeroDes = {1,0,0,0,0,0,0,0,0,0};
    static double[] oneDes = {0,1,0,0,0,0,0,0,0,0};
    static double[] twoDes = {0,0,1,0,0,0,0,0,0,0};
    static double[] threeDes = {0,0,0,1,0,0,0,0,0,0};
    static double[] fourDes = {0,0,0,0,1,0,0,0,0,0};
    static double[] fiveDes = {0,0,0,0,0,1,0,0,0,0};
    static double[] sixDes = {0,0,0,0,0,0,1,0,0,0};
    static double[] sevenDes = {0,0,0,0,0,0,0,1,0,0};
    static double[] eightDes = {0,0,0,0,0,0,0,0,1,0};
    static double[] nineDes = {0,0,0,0,0,0,0,0,0,1};

    static double[][] desiredData = new double[10][10];
    
    public static void main(String args[]) throws IOException
    {  
        trainingData[0] = zero;
        trainingData[1] = one;
        trainingData[2] = two;
        trainingData[3] = three;
        trainingData[4] = four;
        trainingData[5] = five;
        trainingData[6] = six;
        trainingData[7] = seven;
        trainingData[8] = eight;
        trainingData[9] = nine;

        final double rate = 0.1;

        desiredData[0] = zeroDes;
        desiredData[1] = oneDes;
        desiredData[2] = twoDes;
        desiredData[3] = threeDes;
        desiredData[4] = fourDes;
        desiredData[5] = fiveDes;
        desiredData[6] = sixDes;
        desiredData[7] = sevenDes;
        desiredData[8] = eightDes;
        desiredData[9] = nineDes;

        double sumStep = 0;
        double sumLin = 0;
        double sumSig = 0;
        double sumTan = 0;
        double sumRelu = 0;

        //randomly initialize the weights
        for(int x = 0; x<3; x++)
        {
            for(int i = 0; i<45; i++)
            {
                w1[x][i] = (Math.random()*((.5)-(-.5))+(-.5));
                //w1[x][i] = (Math.random()*((2.4/45)-(-2.4/45))+(-2.4/45));
                w1[x][i] = Math.round(w1[x][i]*10000);
                w1[x][i] = w1[x][i]/10000;
            }
        } 
        for(int x = 0; x<3; x++)
        {
            for(int i = 0; i<10; i++)
            {
                //w2[x][i] = (Math.random()*((2.4/10)-(-2.4/10))+(-2.4/10));
                w2[x][i] = (Math.random()*((.5)-(-.5))+(-.5));
                w2[x][i] = Math.round(w2[x][i]*10000);
                w2[x][i] = w2[x][i]/10000;
            }
        }

        sumErrorSq = 101;

        while(sumErrorSq>3)
        {
            sumErrorSq = 0;
            for(int z = 0; z<10; z++)
            {
                //System.out.println("Training on number "+z);
                input = trainingData[z];
                for(int i = 0; i<45; i++)
                {
                    //sumStep += input[i]*w1[0][i];
                    //sumLin += input[i]*w1[1][i];
                    sumSig += input[i]*w1[0][i];
                    sumTan += input[i]*w1[1][i];
                    sumRelu += input[i]*w1[2][i];
                }

                //middleLayer[0] = step(sumStep-1);
                //middleLayer[1] = linear(sumLin-1);
                middleLayer[0] = sigmoid(sumSig-1);
                middleLayer[1] = tanh(sumTan-1);
                //middleLayer[2] = relu(sumRelu-1);

                sumStep = 0;
                sumLin = 0;
                sumSig = 0;
                sumTan = 0;
                sumRelu = 0;

            
                for(int j = 0; j<10; j++)
                {
                    output[j] = sigmoid(middleLayer[0]*w2[0][j]+middleLayer[1]*w2[1][j]-outputThreshhold[j]);
                }


                /**
                 * You are good till here
                 * Review Pinned lecture slide 18 on weight training cause we aint doing it right
                 */
                //System.out.println("Outputs:");
                for(int j = 0; j<10; j++)
                {
                   // System.out.println(output[j]);
                }
                for(int i = 0; i<2; i++)
                {
                    for(int j = 0; j<10; j++)
                    {
                        error[j] = desiredData[z][j] - output[j];
                        /*if(error[j]>10)
                        {
                            error[j] = Math.random()*(10);
                        }*/
                        errorGradient[j] = output[j]*(1-output[j])*error[j];
                        outputWeightAdjustment[j] = rate*middleLayer[i]*errorGradient[j];
                        sumErrorSq += error[j]*error[j];
                        w2[i][j] = w2[i][j]-outputWeightAdjustment[j];
                        outputThreshholdAdjustment[j] = rate*(-1)*errorGradient[j];
                        outputThreshhold[j] = outputThreshhold[j]+outputThreshholdAdjustment[j];
                    }
                    double outputAVG = Arrays.stream(outputWeightAdjustment).sum();
                    outputAVG = outputAVG/10;
                    double w2AVG = Arrays.stream(w2[i]).sum();
                    outputAVG = outputAVG/10;
                    middleGradient[i] = middleLayer[i]*(1-middleLayer[i])*outputAVG*w2AVG;
                    for(int j = 0; j<45; j++)
                    {
                        w1[i][j] = w1[i][j] - rate*middleGradient[i]*input[j];
                    }
                    
                }
                //System.out.println("Errors:");
                for(int j = 0; j<10; j++)
                {
                    //System.out.println(error[j]);
                    //reset error arrays
                    error[j] = 0;
                }
            }
            sumErrorSq = sumErrorSq/10;
            //System.out.println("Sum Errors Squared:"+sumErrorSq);
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("EpochData.txt",true));
                out.write(epoch+","+sumErrorSq+"\n");
                out.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            epoch++;
            
            middleLayer[0] = 0;
            middleLayer[1] = 0;
            middleLayer[2] = 0;
            //middleLayer[3] = 0;
            //middleLayer[4] = 0;
            
        }
        int correct = 0;
        for(int z = 0; z<30;z++)
        {
            input = testData[z];

            for(int i = 0; i<45; i++)
            {
                //sumStep += input[i]*w1[0][i];
                //sumLin += input[i]*w1[1][i];
                sumSig += input[i]*w1[0][i];
                sumTan += input[i]*w1[1][i];
                sumRelu += input[i]*w1[2][i];
            }
            //middleLayer[0] = step(sumStep-1);
            //middleLayer[1] = linear(sumLin-1);
            middleLayer[0] = sigmoid(sumSig-1);
            middleLayer[1] = tanh(sumTan-1);
            //middleLayer[2] = relu(sumRelu-1);

            sumStep = 0;
            sumLin = 0;
            sumSig = 0;
            sumTan = 0;
            sumRelu = 0;

            double max = 0;
            double maxPos = 0;
            for(int j = 0; j<10; j++)
            {
                output[j] = sigmoid(middleLayer[0]*w2[0][j]+middleLayer[1]*w2[1][j]-outputThreshhold[j]);
                if(output[j]>max)
                {
                    max = output[j];
                    maxPos = j;
                }
            }
            max = max*100;
            System.out.println(String.format("%.2f",max)+" % confident value is: "+maxPos);
            if((0<=z)&&(z<=2)&&(maxPos ==1 ))
                correct +=1;
            if((3<=z)&&(z<=5 )&&( maxPos == 2))
                correct +=1;
            if((6<=z)&&(z<=8 )&&( maxPos ==3 ))
                correct +=1;
            if((9<=z)&&(z<=11 )&&( maxPos ==4 ))
                correct +=1;
            if((12<=z)&&(z<=14 )&&( maxPos ==5 ))
                correct +=1;
            if((15<=z)&&(z<=17 )&&( maxPos ==6 ))
                correct +=1;
            if((18<=z)&&(z<=20 )&&( maxPos ==7 ))
                correct +=1;
            if((21<=z)&&(z<=23 )&&( maxPos ==8 ))
                correct +=1;
            if((24<=z)&&(z<=26 )&&( maxPos ==9 ))
                correct +=1;
            if((27<=z)&&(z<=29 )&&( maxPos ==0 ))
                correct +=1;
        }
        System.out.println("Correct: "+correct);
    }

    public static double step(double input)
    {
        double result = 0;
        if(input > result)
        {
            result = 1;
        }
        return result;
    }

    //presenting the most useless function I have ever coded
    public static double linear(double input)
    {
        return input;
    }

    public static double sigmoid(double input)
    {
        double result = java.lang.Math.exp(-input);
        result = 1 + result;
        result = 1 / result;
        return result;
    }

    public static double tanh(double input)
    {
        double numerator = (java.lang.Math.exp(input) - java.lang.Math.exp(-input));
        double denominator = (java.lang.Math.exp(input) + java.lang.Math.exp(-input));
        double result = numerator / denominator;
        return result;
    }

    public static double relu(double input)
    {
        return java.lang.Math.max(0, input);
    }
    
}  