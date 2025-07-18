# PSM – Fundamentals of Computer Simulations (PJATK, 2025)

This repository contains lab assignments from the "Fundamentals of Computer Simulation" course at PJATK.
Each project simulates a physical system using basic numerical methods in Java, often using JFreeChart for visualization.


## Labs Overview

- **Lab 2 – Projectile Motion Simulation**  
  Simulates 2D projectile motion with and without air resistance using two numerical methods:  
  Euler Method and Midpoint (RK2). Includes velocity, position, and time tracking.  
  *Goal: Learn how to model and simulate real-world motion using numerical integration methods.*

---

- **Lab 3 – Pendulum Energy Simulation**  
  Simulates the motion of a simple pendulum using Heun and RK4 methods.  
  Tracks angular dynamics and energy conservation over time.  
  *Goal: Understand energy transformations and numerical stability in oscillatory systems.*
  
![improvedEuler](https://github.com/user-attachments/assets/d9708dcc-2611-4d95-b4d0-eb88fee19324)
![rk4](https://github.com/user-attachments/assets/91d9ef8f-deec-4ed8-8034-d41226d286e5)

---

- **Lab 4 – Rolling Objects on an Incline**  
  Simulates the motion and energy of rolling objects (sphere vs. cylinder) on an inclined plane using Midpoint integration. Includes position, rotation angle, and total energy tracking with live charts.  
  *Goal: Understand the dynamics of rotational motion and compare how moment of inertia affects movement and energy conservation.*
  
![totalEnergy](https://github.com/user-attachments/assets/31236b94-aa83-45d3-8448-6cf858f6f5ca)
![rotationAngle](https://github.com/user-attachments/assets/82dd4e32-cb69-4bfa-81f4-4a288a56541f)
![centerMass](https://github.com/user-attachments/assets/2ac02dfb-3fc1-4fda-9a41-eb3f0bf7a693)

---
- **Lab 5 – Moon–Earth–Sun System Simulation**  
  Simulates the motion of the Earth and the Moon orbiting around the Sun using the Midpoint (Improved Euler) method.  
  Visualizes relative trajectories and exaggerates the Moon’s orbit for clarity.  
  Tracks positions, velocities, and interactions due to gravitational forces between all three bodies.  
  *Goal: Explore multi-body gravitational dynamics and numerical integration stability in orbital mechanics.*

![trajectory](https://github.com/user-attachments/assets/6533d753-7e47-4f71-b870-94878540e0c7)

---

- **Lab 6 – String Wave Simulation**  
  Simulates wave motion on a discretized string using finite-difference methods with explicit time-stepping.  
  Tracks potential, kinetic, and total energies over time.  
  Includes interactive charts of energy evolution and string shapes at multiple time snapshots.  
  *Goal: Practice numerical modeling of wave phenomena and energy conservation in discretized systems. Learn to visualize simulation results with custom charts.*
  
![shapes](https://github.com/user-attachments/assets/1144d481-5f37-4d70-9aa1-f33cd3b1d5a0)
![energies](https://github.com/user-attachments/assets/bdafce4d-a501-4b06-b0b3-f048f869e013)

---

- **Lab 7 – Steady-State Heat Distribution on a Square Plate**  
  Simulates the steady-state temperature distribution on a 2D square plate using the finite-difference method and iterative relaxation.  
  Fixed temperatures are applied at all edges (Dirichlet boundary conditions), and the interior values converge until equilibrium.  
  Renders the final temperature field as a color map in a Java Swing window.  
  *Goal: Learn to model and visualize stationary solutions of Laplace's equation in 2D using numerical methods.*

![temperature](https://github.com/user-attachments/assets/de2fc0be-771d-47c0-93ec-032aee1281f6)

---

- **Lab 8 – Lorenz System Simulation**  
  Simulates the chaotic Lorenz system of differential equations using three numerical methods:  
  Euler, Midpoint (RK2), and Runge–Kutta 4 (RK4).  
  Plots the x–z projection of each trajectory to compare numerical stability and divergence.  
  *Goal: Understand chaotic dynamics, sensitivity to initial conditions, and accuracy of integration methods for stiff systems.*
  
![system](https://github.com/user-attachments/assets/18f60cc2-0772-4ee7-8bf9-22f2b1e67c3b)


---

- **Lab 9 – Fractal Plant with L-System**  
  Generates and renders a fractal "plant" structure using an L-system with branching rules.  
  Applies iterative string rewriting to produce complex patterns of growth.  
  Renders the final structure in Java Swing using turtle-graphics-style interpretation of symbols.  
  *Goal: Learn procedural generation techniques with formal grammars and visualize self-similar structures like plants.*

  
![plant](https://github.com/user-attachments/assets/4c641587-7f14-41e1-b2c1-af32e20a9602)

---

- **Lab 10 – Conway's Game of Life**  
  Implements Conway’s Game of Life with customizable birth and survival rules (e.g. B3/S23).  
  Features an interactive Swing GUI with start/pause, clear, randomize, and rule-setting controls.  
  Users can toggle cells by clicking, visualize generations in real time, and experiment with custom rule sets.  
  *Goal: Learn about cellular automata, emergent behavior from simple rules, and GUI-based simulation control.*

<img width="496" height="554" alt="gameOfLife" src="https://github.com/user-attachments/assets/da66d3a3-106d-439a-9785-c3d63d6d9b6c" />



