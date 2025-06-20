import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main{
    static class RollingObject{
        double position,velocity,rotationAngle,angularVelocity;
        double mass,radius,inertiaRatio;

        public RollingObject(double mass,double radius,double inertiaRatio){
            this.mass=mass;
            this.radius=radius;
            this.inertiaRatio=inertiaRatio;
            position=0;
            velocity=0;
            rotationAngle=0;
            angularVelocity=0;
        }

        public void updateState(double timeStep,double gravity,double inclineAngle){
            double acceleration=gravity*Math.sin(inclineAngle)/(1+inertiaRatio);
            double midVelocity=velocity+0.5*timeStep*acceleration;
            position=position+timeStep*midVelocity;
            velocity=velocity+timeStep*acceleration;
            double midAngularVelocity=angularVelocity+0.5*timeStep*(acceleration/radius);
            rotationAngle=rotationAngle+timeStep*midAngularVelocity;
            angularVelocity=angularVelocity+timeStep*(acceleration/radius);
        }

        public double getTranslationalKineticEnergy(){
            return 0.5*mass*velocity*velocity;
        }

        public double getRotationalKineticEnergy(){
            double momentOfInertia=inertiaRatio*mass*radius*radius;
            return 0.5*momentOfInertia*angularVelocity*angularVelocity;
        }

        public double getPotentialEnergy(double gravity,double initialHeight,double inclineAngle){
            double height=initialHeight-position*Math.sin(inclineAngle);
            return mass*gravity*height;
        }

        public double getTotalEnergy(double gravity,double initialHeight,double inclineAngle){
            return getTranslationalKineticEnergy()+getRotationalKineticEnergy()+getPotentialEnergy(gravity,initialHeight,inclineAngle);
        }
    }

    public static void main(String[] args){
        double gravity=9.81;
        double inclineAngle=Math.toRadians(30);
        double timeStep=0.01;
        double totalTime=5.0;
        int numberOfSteps=(int)(totalTime/timeStep);
        double initialHeight=1.0;

        RollingObject sphere=new RollingObject(1.0,0.1,2.0/5.0);
        RollingObject otherObject=new RollingObject(1.01,0.1,2.0/3.0);

        List<Double> timeList=new ArrayList<>();
        List<Double> spherePositionList=new ArrayList<>();
        List<Double> sphereRotationAngleList=new ArrayList<>();
        List<Double> objectPositionList=new ArrayList<>();
        List<Double> objectRotationAngleList=new ArrayList<>();
        List<Double> sphereEnergyList=new ArrayList<>();
        List<Double> objectEnergyList=new ArrayList<>();

        double currentTime=0.0;
        for(int i=0;i<=numberOfSteps;i++){
            timeList.add(currentTime);
            spherePositionList.add(sphere.position);
            sphereRotationAngleList.add(sphere.rotationAngle);
            objectPositionList.add(otherObject.position);
            objectRotationAngleList.add(otherObject.rotationAngle);
            sphereEnergyList.add(sphere.getTotalEnergy(gravity,initialHeight,inclineAngle));
            objectEnergyList.add(otherObject.getTotalEnergy(gravity,initialHeight,inclineAngle));

            System.out.printf(Locale.US,"Time: %.3f, Sphere Position: %.5f, Sphere Rotation: %.5f, Sphere Energy: %.5f, Other Position: %.5f, Other Rotation: %.5f, Other Energy: %.5f%n",currentTime,sphere.position,sphere.rotationAngle,sphere.getTotalEnergy(gravity,initialHeight,inclineAngle),otherObject.position,otherObject.rotationAngle,otherObject.getTotalEnergy(gravity,initialHeight,inclineAngle));

            sphere.updateState(timeStep,gravity,inclineAngle);
            otherObject.updateState(timeStep,gravity,inclineAngle);
            currentTime+=timeStep;
        }

        try(PrintWriter writer=new PrintWriter(new FileWriter("simulationData.csv"))){
            writer.println("time,spherePosition,sphereRotation,otherPosition,otherRotation,sphereEnergy,otherEnergy");
            for(int i=0;i<timeList.size();i++){
                writer.printf(Locale.US,"%.5f,%.5f,%.5f,%.5f,%.5f,%.5f,%.5f%n",timeList.get(i),spherePositionList.get(i),sphereRotationAngleList.get(i),objectPositionList.get(i),objectRotationAngleList.get(i),sphereEnergyList.get(i),objectEnergyList.get(i));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
