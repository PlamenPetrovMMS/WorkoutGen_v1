package com.example.workoutgenerator;

import androidx.annotation.NonNull;

public class Exercise {
    public static final Exercise.MovementType DEFAULT_MOVEMENT_TYPE = Exercise.MovementType.ANY;
    public static final Exercise.BodypartType DEFAULT_BODYPART_TYPE = Exercise.BodypartType.ANY;
    public static final Exercise.Symmetry DEFAULT_SYMMETRY = Exercise.Symmetry.NONE;
    public static final Exercise.Level DEFAULT_LEVEL = Level.ANY;
    public static int NEXT_ID = 0;
    private int id;
    private String name;
    private Level level;
    private MovementType movementType;
    private BodypartType bodypartType;
    private Symmetry symmetry;


    // ===== ENUMS =====

    public enum Level{
        EASY(0),
        MEDIUM(1),
        HARD(2),
        ANY(3);

        private final int value;

        Level(int value) {
            this.value = value;
        }
        public int getValue(){
            return value;
        }
        public static Level fromInt(int value){
            for(Level level: Level.values()){
                if(level.getValue() == value){
                    return level;
                }
            }
            throw new IllegalArgumentException("Invalid level value: " + value);
        }
        public static Level fromString(String string){
            for(Level level: Level.values()){
                if(level.name().equals(string)){
                    return level;
                }
            }
            throw new IllegalArgumentException("Invalid level type string: " + string);
        }
        @NonNull
        @Override
        public String toString() {
            return name().toUpperCase();
        }

    }

    public enum MovementType{
        PUSH(0),
        PULL(1),
        ANY(2);

        private final int value;

        MovementType(int value) {
            this.value = value;
        }
        public int getValue(){
            return value;
        }
        public static MovementType fromInt(int value){
            for(MovementType mt: MovementType.values()){
                if(mt.getValue() == value){
                    return mt;
                }
            }
            throw new IllegalArgumentException("Invalid movement type value: " + value);
        }
        public static MovementType fromString(String string){
            for(MovementType mt: MovementType.values()){
                if(mt.name().equals(string)){
                    return mt;
                }
            }
            throw new IllegalArgumentException("Invalid movement type string: " + string);
        }

        @NonNull
        @Override
        public String toString() {
            return name().toUpperCase();
        }
    }

    public enum BodypartType{

        UPPERBODY(0),
        CORE(1),
        LOWERBODY(2),
        ANY(3);


        private final int value;

        BodypartType(int value) {
            this.value = value;
        }
        public int getValue(){
            return value;
        }
        public static BodypartType fromInt(int value){
            for(BodypartType bodypartType: BodypartType.values()){
                if(bodypartType.getValue() == value){
                    return bodypartType;
                }
            }
            throw new IllegalArgumentException("Invalid bodypart type value: " + value);
        }
        public static BodypartType fromString(String string){
            for(BodypartType bodypartType: BodypartType.values()){
                if(bodypartType.name().equals(string)){
                    return bodypartType;
                }
            }
            throw new IllegalArgumentException("Invalid bodypart type string: " + string);
        }

        @NonNull
        @Override
        public String toString() {
            return name().toUpperCase();
        }

    }

    public enum Symmetry{
        UNILATERAL(0), // perform one side at a time
        BILATERAL(1), // perform for both sides
        NONE(2);

        private final int value;

        Symmetry(int value) {
            this.value = value;
        }
        public int getValue(){
            return value;
        }
        public static Symmetry fromInt(int value){
            for(Symmetry s: Symmetry.values()){
                if(s.getValue() == value){
                    return s;
                }
            }
            throw new IllegalArgumentException("Invalid symmetry value: " + value);
        }
        public static Symmetry fromString(String string){
            for(Symmetry symmetry: Symmetry.values()){
                if(symmetry.name().equals(string)){
                    return symmetry;
                }
            }
            throw new IllegalArgumentException("Invalid symmetry type string: " + string);
        }
        @NonNull
        @Override
        public String toString() {
            return name().toUpperCase();
        }
    }


    // ===== CONSTRUCTORS =====

    public Exercise(){
        id = NEXT_ID;
        name = null;
        level = DEFAULT_LEVEL;
        movementType = DEFAULT_MOVEMENT_TYPE;
        bodypartType = DEFAULT_BODYPART_TYPE;
        symmetry = DEFAULT_SYMMETRY;

        NEXT_ID++;
    }
    public Exercise(int id, String name, Level level, MovementType movementType, BodypartType bodypartType, Symmetry symmetry){
        this.id = id;
        this.name = name;
        this.level = level;
        this.movementType = movementType;
        this.bodypartType = bodypartType;
        this.symmetry = symmetry;

        if(NEXT_ID == id){
            NEXT_ID++;
        }
    }





    // ===== SET METHODS =====
    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setLevel(Level level){
        this.level = level;
    }
    public void setMovementType(MovementType movementType){
        this.movementType = movementType;
    }
    public void setBodypartType(BodypartType bodypartType){
        this.bodypartType = bodypartType;
    }
    public void setSymmetry(Symmetry symmetry){
        this.symmetry = symmetry;
    }






    // ===== GET METHODS =====
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Level getLevel(){
        return level;
    }
    public MovementType getMovementType(){
        return movementType;
    }
    public BodypartType getBodypartType(){
        return bodypartType;
    }
    public Symmetry getSymmetry(){
        return symmetry;
    }

    // ===== FUNCTIONS =====


    public boolean isExerciseValid(){
        if(name == null || name.trim().isEmpty()) return false;
        if(movementType == null) return false;
        if(level == null || level == Level.ANY) return false;
        if(bodypartType == null || bodypartType == BodypartType.ANY) return false;
        if(symmetry == null || symmetry == Symmetry.NONE) return false;

        return true;
    }

} // Exercise class ends here ======================================================================
