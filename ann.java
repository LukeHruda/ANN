import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ann  
{  
    //declare arrays and training / testing data
    static int epoch = 1;
    static double[] input = new double[45];
    static double[][] w1 = new double[2][45];
    static double[] middleLayer = {0,0};
    static double[][] w2 = new double[3][10];
    static double[] output = {0,0,0,0,0,0,0,0,0,0};
    static double[] error = new double[10];
    static double[] errorGradient = new double[10];
    static double[] middleGradient = new double[2];
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
        //initialize the full training data array
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

        //set the learning rate
        final double rate = 0.1;

        //initialize the correect answers data set
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

        //define the input values to the sig and tanh functions
        double sumSig = 0;
        double sumTan = 0;

        //randomly initialize the weights
        for(int x = 0; x<2; x++)
        {
            for(int i = 0; i<45; i++)
            {
                w1[x][i] = (Math.random()*((.5)-(-.5))+(-.5));
                w1[x][i] = Math.round(w1[x][i]*10000);
                w1[x][i] = w1[x][i]/10000;
            }
        } 
        for(int x = 0; x<2; x++)
        {
            for(int i = 0; i<10; i++)
            {
                w2[x][i] = (Math.random()*((.5)-(-.5))+(-.5));
                w2[x][i] = Math.round(w2[x][i]*10000);
                w2[x][i] = w2[x][i]/10000;
            }
        }

        //traing while the average sum of errors squared is above 2
        sumErrorSq = 3;
        while(sumErrorSq>2)
        {
            sumErrorSq = 0;
            //loop through each training number
            for(int z = 0; z<10; z++)
            {
                System.out.println("Training on number "+z);
                input = trainingData[z];
                //multiply the input layer by its respective weight to get the sum to feed to activation functions
                for(int i = 0; i<45; i++)
                {
                    sumSig += input[i]*w1[0][i];
                    sumTan += input[i]*w1[1][i];
                }
                //feed to activation function
                middleLayer[0] = sigmoid(sumSig-1);
                middleLayer[1] = tanh(sumTan-1);

                //reset the sums
                sumSig = 0;
                sumTan = 0;

                //get the output value by multiplying the activation function output with its weight, add them together and subtract threshold value
                for(int j = 0; j<10; j++)
                {
                    output[j] = sigmoid(middleLayer[0]*w2[0][j]+middleLayer[1]*w2[1][j]-outputThreshhold[j]);
                }

                //print the ouputs
                System.out.println("Outputs:");
                for(int j = 0; j<10; j++)
                {
                   System.out.println(output[j]);
                }
                //go through the weights and train them
                for(int i = 0; i<2; i++)
                {
                    for(int j = 0; j<10; j++)
                    {
                        //get the error
                        error[j] = desiredData[z][j] - output[j];

                        //get the error gradient
                        errorGradient[j] = output[j]*(1-output[j])*error[j];

                        //get the output weight adjustment value
                        outputWeightAdjustment[j] = rate*middleLayer[i]*errorGradient[j];

                        //calculate the sum of the squared error
                        sumErrorSq += error[j]*error[j];

                        //adjust the output weights
                        w2[i][j] = w2[i][j]-outputWeightAdjustment[j];

                        //calculate the ouptput threshold adjustment value and update it
                        outputThreshholdAdjustment[j] = rate*(-1)*errorGradient[j];
                        outputThreshhold[j] = outputThreshhold[j]+outputThreshholdAdjustment[j];
                    }
                    //calculate the average of the ouptput weight adjustment
                    double outputAVG = Arrays.stream(outputWeightAdjustment).sum();
                    outputAVG = outputAVG/10;

                    //calculate the middle layer gradient value and adjust them
                    middleGradient[i] = middleLayer[i]*(1-middleLayer[i])*outputAVG;
                    for(int j = 0; j<45; j++)
                    {
                        w1[i][j] = w1[i][j] - rate*middleGradient[i]*input[j];
                    }
                    
                }
                //print the list of errors and reset them
                System.out.println("Errors:");
                for(int j = 0; j<10; j++)
                {
                    System.out.println(error[j]);
                    //reset error arrays
                    error[j] = 0;
                }
            }
            //get the average error and print it
            sumErrorSq = sumErrorSq/10;
            System.out.println("Sum Errors Squared:"+sumErrorSq);
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("EpochData.txt",true));
                out.write(epoch+","+sumErrorSq+"\n");
                out.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            epoch++;
            //reset the middle layer
            middleLayer[0] = 0;
            middleLayer[1] = 0;           
        }
        //feed in the 30 test cases and determine if the output is correct or not
        int correct = 0;
        for(int z = 0; z<30;z++)
        {
            input = testData[z];

            for(int i = 0; i<45; i++)
            {
                sumSig += input[i]*w1[0][i];
                sumTan += input[i]*w1[1][i];
            }
            middleLayer[0] = sigmoid(sumSig-1);
            middleLayer[1] = tanh(sumTan-1);

            sumSig = 0;
            sumTan = 0;

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

    //sigmoid function
    public static double sigmoid(double input)
    {
        double result = java.lang.Math.exp(-input);
        result = 1 + result;
        result = 1 / result;
        return result;
    }

    //tanh function
    public static double tanh(double input)
    {
        double numerator = (java.lang.Math.exp(input) - java.lang.Math.exp(-input));
        double denominator = (java.lang.Math.exp(input) + java.lang.Math.exp(-input));
        double result = numerator / denominator;
        return result;
    }
    
}  
