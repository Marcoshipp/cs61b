// all methods should be none static since we're never gonna run the Planet.java
public class Planet {
    // instance vars
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public static final double G = 6.67e-11;

    // the two constructors
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    // calculate the distant of two planets
    public double calcDistance(Planet p) {
        double dx = this.xxPos - p.xxPos;
        double dy = this.yyPos - p.yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calcForceExertedBy(Planet p) {
        // Gmm / r^2
        double r = calcDistance(p);
        return (G * this.mass * p.mass) / (r * r);
    }

    public double calcForceExertedByX(Planet p) {
        // F*dx / r
        double r = calcDistance(p);
        double force = calcForceExertedBy(p);
        double dx = p.xxPos - this.xxPos;
        return (force * dx) / r;
    }

    public double calcForceExertedByY(Planet p) {
        // Gmm / r^2
        double r = calcDistance(p);
        double force = calcForceExertedBy(p);
        double dy = p.yyPos - this.yyPos;
        return (force * dy) / r;
    }

    public double calcNetForceExertedByX(Planet[] allPlanets) {
        double sum = 0;
        for (Planet p : allPlanets) {
            if (this.equals(p)) {
                continue;
            }
            sum += this.calcForceExertedByX(p);
        }
        return sum;
    }

    public double calcNetForceExertedByY(Planet[] allPlanets) {
        double sum = 0;
        for (Planet p : allPlanets) {
            if (this.equals(p)) {
                continue;
            }
            sum += this.calcForceExertedByY(p);
        }
        return sum;
    }

    public void update(double dt, double fX, double fY) {
        // F = MA
        double ax = fX / this.mass;
        double ay = fY / this.mass;
        this.xxVel += dt * ax;
        this.yyVel += dt * ay;
        this.xxPos += dt * this.xxVel;
        this.yyPos += dt * this.yyVel;
    }

    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
    }
}
