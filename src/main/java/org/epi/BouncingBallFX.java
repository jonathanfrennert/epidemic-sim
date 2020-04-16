package org.epi;
/*
   BouncingBallFX.java Copyright (c) Kari Laitinen

   http://www.naturalprogramming.com/

   2014-12-18 File created.
   2016-09-29 Last modification.


    This program shows a ball, or a bouncer, that moves
    on the screen. The ball bounces when it hits a 'wall'.
    The bouncer will explode when the Escape key is pressed.

    This program has the following hierarchy of classes:

    Bouncer extends Group

       - represents a ball object that can move and bounce
         inside a given rectangular area
       - Group is the base class because in lower classes
         the bouncer consists of several 'layers'

    RotatingBouncer extends Bouncer

       - represents a bouncer that rotates while it is moving

    ExplodingBouncer extends RotatingBouncer

       - a rotating bouncer that can be made to explode
         and disappear

*/


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

class Bouncer extends Group
{
    // bouncer_velocity specifies the number of pixels the object
    // will be moved in a single movement operation.
    double bouncer_velocity   =  2.0 ;
    double bouncer2_velocity = 1.0;

    // bouncer_direction is an angle in radians. This angle specifies
    // the direction where the bouncer will be moved next.
    double bouncer_direction  =  Math.random() * Math.PI * 2 ;
    double bouncer2_direction = Math.random() * Math.PI * 2;

    Circle bouncer_background ;
    Circle bouncer_2;

    Rectangle  bouncing_area ;

    double last_movement_x, last_movement_y ;
    double last_movement_x2, last_movement_y2;

    public Bouncer( Point2D   given_position,
                    Color     given_color,
                    Rectangle given_bouncing_area )
    {
        bouncer_background = new Circle( given_position.getX(),
                given_position.getY(),
                64, given_color ) ;

        bouncer_2 = new Circle( given_position.getX(),
                given_position.getY(),
                64, given_color);

        bouncer_background.setStroke( Color.BLACK ) ;
        bouncer_2.setStroke(Color.BLACK);

        bouncing_area  =  given_bouncing_area ;

        getChildren().add( bouncer_background ) ;
        getChildren().add(bouncer_2);
    }


    public void shrink()
    {
        //  The if-construct ensures that the ball does not become
        //  too small.

        if ( bouncer_background.getRadius()  > 5 )
        {
            bouncer_background.setRadius( bouncer_background.getRadius() - 3 ) ;
        }
    }


    public void enlarge()
    {
        bouncer_background.setRadius( bouncer_background.getRadius() + 3 ) ;
    }


    // With the following method it is possible to check whether the given
    // point is within the ball area, in the host coordinate system.

    public boolean contains_point( double given_point_x, double given_point_y )
    {
        //  Here we use the Pythagorean theorem to calculate the distance
        //  from the given point to the center point of the ball.

        double  distance_from_given_point_to_ball_center  =

                Math.sqrt(

                        Math.pow( bouncer_background.getCenterX()  -  given_point_x, 2 )  +
                                Math.pow( bouncer_background.getCenterY()  -  given_point_y, 2 )  ) ;

        return ( distance_from_given_point_to_ball_center  <=
                bouncer_background.getRadius() ) ;
    }


    public void move()
    {
        //  In the following statement a minus sign is needed when the
        //  y coordinate is calculated. The reason for this is that the
        //  y direction in the graphical coordinate system is 'upside down'.

        last_movement_x = bouncer_velocity * Math.cos(bouncer_direction ) ;
        last_movement_y = - bouncer_velocity * Math.sin(bouncer_direction ) ;

        last_movement_x2 = bouncer2_velocity * Math.cos(bouncer2_direction);
        last_movement_y2 = - bouncer2_velocity * Math.sin(bouncer2_direction);

        bouncer_background.setCenterX(
                bouncer_background.getCenterX() + last_movement_x ) ;
        bouncer_background.setCenterY(
                bouncer_background.getCenterY() + last_movement_y ) ;

        bouncer_2.setCenterX(bouncer_2.getCenterX() + last_movement_x2);
        bouncer_2.setCenterY(bouncer_2.getCenterY() + last_movement_y2);

        //  Now, after we have moved this bouncer, we start finding out whether
        //  or not it has hit a wall or some other obstacle. If a hit occurs,
        //  a new direction for the bouncer must be calculated.

        //  The following four if constructs must be four separate ifs.
        //  If they are replaced with an if - else if - else if - else if
        //  construct, the program will not work when the bouncer enters
        //  a corner in an angle of 45 degrees (i.e. Math.PI / 4).

        if ( bouncer_background.getCenterY() - bouncer_background.getRadius()
                <=  bouncing_area.getY() )
        {
            //  The bouncer has hit the northern 'wall' of the bouncing area.

            bouncer_direction = 2 * Math.PI - bouncer_direction ;
        }

        if ( bouncer_background.getCenterX() - bouncer_background.getRadius()
                <=  bouncing_area.getX() )
        {
            //  The western wall has been reached.

            bouncer_direction = Math.PI - bouncer_direction ;
        }

        if ( ( bouncer_background.getCenterY()  +  bouncer_background.getRadius() )
                >= ( bouncing_area.getY()  +  bouncing_area.getHeight() ) )
        {
            //  Southern wall has been reached.

            bouncer_direction = 2 * Math.PI - bouncer_direction ;
        }

        if ( ( bouncer_background.getCenterX()  +  bouncer_background.getRadius() )
                >= ( bouncing_area.getX()  +  bouncing_area.getWidth() ) )
        {
            //  Eastern wall reached.

            bouncer_direction = Math.PI - bouncer_direction ;
        }

        //TODO make method out of if statements
        if ( bouncer_2.getCenterY() - bouncer_2.getRadius()
                <=  bouncing_area.getY() )
        {
            //  The bouncer has hit the northern 'wall' of the bouncing area.

            bouncer2_direction = 2 * Math.PI - bouncer2_direction ;
        }

        if ( bouncer_2.getCenterX() - bouncer_2.getRadius()
                <=  bouncing_area.getX() )
        {
            //  The western wall has been reached.

            bouncer2_direction = Math.PI - bouncer2_direction ;
        }

        if ( ( bouncer_2.getCenterY()  +  bouncer_2.getRadius() )
                >= ( bouncing_area.getY()  +  bouncing_area.getHeight() ) )
        {
            //  Southern wall has been reached.

            bouncer2_direction = 2 * Math.PI - bouncer2_direction ;
        }

        if ( ( bouncer_2.getCenterX()  +  bouncer_2.getRadius() )
                >= ( bouncing_area.getX()  +  bouncing_area.getWidth() ) )
        {
            //  Eastern wall reached.

            bouncer2_direction = Math.PI - bouncer2_direction ;
        }

        if(Math.abs(bouncer_2.getCenterX() - bouncer_background.getCenterX()) <
                bouncer_2.getRadius() + bouncer_background.getRadius()
        && Math.abs(bouncer_2.getCenterY() - bouncer_background.getCenterY()) <
                bouncer_2.getRadius() + bouncer_background.getRadius()){
            bouncer_direction = 2 * Math.PI - bouncer_direction;
            bouncer2_direction = 2 * Math.PI - bouncer2_direction;
        }

    }

}



class RotatingBouncer extends Bouncer
{
    Shape rotating_layer ;

    public RotatingBouncer(  Point2D   given_position,
                             Color     given_color,
                             Rectangle given_bouncing_area )
    {
        super( given_position, given_color, given_bouncing_area ) ;

        // Here, we'll construct a rotating_layer which will be
        // rotated over the bouncer_background. The rotating_layer is
        // a union of two filled Arc objects.

        Arc north_east_arc = new Arc( bouncer_background.getCenterX(),
                bouncer_background.getCenterY(),
                bouncer_background.getRadius(),
                bouncer_background.getRadius(),
                0, 90 ) ;

        north_east_arc.setType( ArcType.ROUND ) ;

        Arc south_west_arc = new Arc( bouncer_background.getCenterX(),
                bouncer_background.getCenterY(),
                bouncer_background.getRadius(),
                bouncer_background.getRadius(),
                180, 90 ) ;

        south_west_arc.setType( ArcType.ROUND ) ;


        rotating_layer = Shape.union( north_east_arc, south_west_arc ) ;
        rotating_layer.setFill( Color.GREEN ) ;

        getChildren().add( rotating_layer ) ;
    }


    public void move()
    {
        super.move() ; // run the corresponding upper class method first

        // The rotating_layer must move so that it will be exactly over the
        // bouncer_background. We'll translate the coordinates according to how
        // the ball was moved by the upperclass method.

        rotating_layer.setTranslateX( rotating_layer.getTranslateX() + last_movement_x ) ;
        rotating_layer.setTranslateY( rotating_layer.getTranslateY() + last_movement_y ) ;

        rotating_layer.setRotate( rotating_layer.getRotate() + 2 ) ;

        if ( rotating_layer.getRotate() >= 360 )
        {
            rotating_layer.setRotate( 0 ) ;
        }
    }
}


class ExplodingBouncer extends RotatingBouncer
{
    static final int BALL_ALIVE_AND_WELL  =  0 ;
    static final int EXPLOSION_REQUESTED  =  1 ;
    static final int BALL_EXPLODING       =  2 ;
    static final int BALL_EXPLODED        =  3 ;

    int ball_state  =  BALL_ALIVE_AND_WELL ;

    double explosion_color_alpha_value = 0.0 ;

    Circle explosion_layer ;

    public ExplodingBouncer( Point2D   given_position,
                             Color     given_color,
                             Rectangle given_bouncing_area )
    {
        super( given_position, given_color, given_bouncing_area ) ;
    }

    public void explode_ball()
    {
        // With the following if construct we ensure that the
        // ball can be exploded only once.

        if ( ball_state == BALL_ALIVE_AND_WELL )
        {
            ball_state = EXPLOSION_REQUESTED ;
        }
    }


    public void move()
    {
        //  The ball will not move if it is exploding or exploded.

        if ( ball_state == BALL_ALIVE_AND_WELL )
        {
            super.move() ; // move the ball with the superclass method
        }
        else if ( ball_state == EXPLOSION_REQUESTED )
        {
            ball_state = BALL_EXPLODING ;
            enlarge() ; // make the ball somewhat larger in explosion
            enlarge() ;

            // The color for the explosion layer will be fully transparent
            // yellow when the 'explosion' starts.
            Color initial_explosion_color =
                    new Color( 1.0, 1.0, 0.0, explosion_color_alpha_value ) ;

            explosion_layer = new Circle( bouncer_background.getCenterX(),
                    bouncer_background.getCenterY(),
                    bouncer_background.getRadius() + 6,
                    initial_explosion_color ) ;

            getChildren().add( explosion_layer ) ;
        }
        else if ( ball_state == BALL_EXPLODING )
        {
            if ( explosion_color_alpha_value > 0.98 )
            {
                ball_state = BALL_EXPLODED ;

                getChildren().clear() ; // This removes all ball layers.
            }
            else
            {
                // The ball will be 'exploded' by having a transparent
                // yellow ball over the original ball.
                // As the opaqueness of the yellow color gradually increases,
                // the ball becomes ultimately completely yellow in
                // the final stage of the explosion.

                explosion_color_alpha_value += 0.02 ; // decrease transparency

                explosion_layer.setFill(
                        new Color( 1.0, 1.0, 0.0, explosion_color_alpha_value ) ) ;

            }
        }
    }
}



public class BouncingBallFX extends Application
{
    static final int SCENE_WIDTH   =  800 ;
    static final int SCENE_HEIGHT  =  680 ;

    AnimationTimer animation_timer ;

    public void start( Stage stage )
    {
        Group group_for_balls = new Group() ;

        stage.setTitle( "BouncingBallFX.java" ) ;

        Scene scene = new Scene( group_for_balls, SCENE_WIDTH, SCENE_HEIGHT ) ;

        scene.setFill( Color.LIGHTYELLOW ) ;

        Rectangle  bouncing_area  =  new Rectangle( 0, 0, SCENE_WIDTH, SCENE_HEIGHT ) ;

        ExplodingBouncer ball_on_screen = new ExplodingBouncer( new Point2D( SCENE_WIDTH / 2,
                SCENE_HEIGHT / 2 ),
                Color.LIME,
                bouncing_area ) ;

        group_for_balls.getChildren().add( ball_on_screen ) ;

        scene.setOnKeyPressed( ( KeyEvent event ) ->
        {
            if ( event.getCode()  ==  KeyCode.ESCAPE )
            {
                ball_on_screen.explode_ball() ;
            }
        } ) ;

        stage.setScene( scene ) ;
        stage.show() ;


        animation_timer = new AnimationTimer()
        {
            public void handle( long timestamp_of_current_frame )
            {
                ball_on_screen.move() ;
            }
        } ;


        animation_timer.start() ;
    }

    public static void main( String[] command_line_parameters )
    {
        launch( command_line_parameters ) ;
    }
}


