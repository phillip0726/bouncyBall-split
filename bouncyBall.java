package hw4;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class bounceBall extends Frame implements ActionListener{

	private Canvas canvas;
	static ArrayList<Ball> BallList;
	public bounceBall(String str) {
		super(str);
		canvas=new Canvas();
		super.add("Center",canvas);
		Panel p=new Panel();
		Button s=new Button("Start");
		Button c= new Button ("Close");
		p.add(s);
		p.add(c);
		s.addActionListener(this);
		c.addActionListener(this);
		add("South",p);
	}
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand()=="Start") {
			BallList=new ArrayList<Ball>();
			BallList.add(new Ball(canvas,200,150,3,1,20,20,0));
			BallList.add(new Ball(canvas,250,150,-3,-2,20,20,100));
			BallList.add(new Ball(canvas,150,150,-2,2,20,20,200));
			BallList.add(new Ball(canvas,150,110,1,-3,20,20,300));
			BallList.add(new Ball(canvas,200,190,1,3,20,20,400));
			
			for(int i=0;i<BallList.size();i++)
				BallList.get(i).start();
		}
		else if(evt.getActionCommand()=="Close")
			System.exit(0);
	}
	public static void main(String[] args) {
		Frame f=new bounceBall("Bounce");
		f.setSize(400,300);
		WindowDestroyer listener = new WindowDestroyer();
		f.addWindowListener(listener);
		f.setVisible(true);
	}
	class Ball extends Thread{
		Canvas box;
		int XSIZE;
		int YSIZE;
		int x=0;
		int y=0;
		int dx=2;
		int dy=2;
		int ballnum=0;
		int r;
		boolean ball_stop=false;
		boolean first_made=false;
		public Ball(Canvas c, int _x, int _y, int _dx, int _dy, int _XSIZE, int _YSIZE, int _ballnum) {
			box=c;
			x=_x;
			y=_y;
			dx=_dx;
			dy=_dy;
			XSIZE=_XSIZE;
			YSIZE=_YSIZE;
			ballnum=_ballnum;
			r=_XSIZE;
		}
		public void draw() {
			Graphics g= box.getGraphics();
			g.fillOval(x, y, XSIZE, YSIZE);
			g.dispose();
		}
		public void move() {
			Graphics g=box.getGraphics();
			g.setXORMode(box.getBackground());

			g.clearRect(x, y, XSIZE, YSIZE);
			//g.fillOval(x, y, XSIZE, YSIZE);

			x+=dx; y+=dy;
			Dimension d=box.getSize();
			if(x<0) {
				x=0; dx=-dx;
			}
			if(x+XSIZE>=d.width) {
				x=d.width-XSIZE; dx=-dx;
			}
			if(y<0) {
				y=0;dy=-dy;
			}
			if(y+YSIZE>=d.height) {
				y=d.height-YSIZE; dy=-dy;
			}

			for(int i=0;i<BallList.size();i++) {
				Ball tmp=BallList.get(i);
				if(this.isBallCrash(tmp) && i!=BallList.indexOf(this)) {
					this.split(this, tmp);
				}
			}
			g.fillOval(x, y, XSIZE, YSIZE);
			g.dispose();
		}
		public boolean isBallCrash(Ball b)
		{
			double distance=Math.sqrt(((this.x-b.x)*(this.x-b.x))+(this.y-b.y)*(this.y-b.y));
			double new_distance=Math.sqrt((this.x+this.dx-b.x-b.dx)*(this.x+this.dx-b.x-b.dx)+(this.y+this.dy-b.y-b.dy)*(this.y+this.dy-b.y-b.dy));
			if((b.r+this.r)/2.0>=distance){
				if(distance>new_distance)
					return true;
			}
			return false;
		}
		public void split(Ball b1, Ball b2) {
			splitInTwo(b1);
			splitInTwo(b2);
		}
		public void splitInTwo(Ball b) {
			if(b.r/2>1) {
				Ball b1=new Ball(b.box,b.x,b.y,(-1)*b.dx,(-1)*b.dy, b.XSIZE/2, b.YSIZE/2, b.ballnum+1 );
				Ball b2=new Ball(b.box,b.x,b.y,b.dx,(-1)*b.dy, b.XSIZE/2, b.YSIZE/2, b.ballnum+1 );
				
				BallList.add(b1);
				BallList.add(b2);
				b1.first_made=true; b2.first_made=true;
	
				b.ball_stop=true;
				
				BallList.remove(b);
				b1.start(); b2.start();
			}
			else {
				System.out.println("Bye Bye.");
				System.exit(0);
				b.ball_stop=true;
				BallList.remove(b);
			}
		} 
		public void run() {
			draw(); 
			while(!ball_stop && XSIZE>1) {
				move();
				try {
					Thread.sleep(30);
				}
				catch(InterruptedException e) {
				}
			}
			box.getGraphics().clearRect(x, y, XSIZE, YSIZE);
		}
	}

}
