"""
Minecraft Feature Worldgen Playground

A tool for testing and visualizing Minecraft feature generation algorithms.
"""

import random
from typing import Callable, Dict, Optional, Tuple

import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.mplot3d.art3d import Poly3DCollection


class World:
	"""Represents a 3D world that can store blocks with RGB colors."""
	
	def __init__(self):
		self.blocks: Dict[Tuple[int, int, int], Tuple[int, int, int]] = {}
	
	def set_block(self, coords: Tuple[int, int, int], color: Tuple[int, int, int]):
		"""
		Set a block at the given coordinates with the specified RGB color.
		
		Args:
			coords: (x, y, z) tuple of block coordinates
			color: (r, g, b) tuple of RGB values (0-255)
		"""
		x, y, z = coords
		r, g, b = color
		
		# Clamp color values to valid range
		r = max(0, min(255, r))
		g = max(0, min(255, g))
		b = max(0, min(255, b))
		
		self.blocks[coords] = (r, g, b)
	
	def get_block(self, coords: Tuple[int, int, int]) -> Optional[Tuple[int, int, int]]:
		"""Get the color of a block at the given coordinates, or None if no block exists."""
		return self.blocks.get(coords)
	
	def clear(self):
		"""Clear all blocks from the world."""
		self.blocks.clear()
	
	def get_all_blocks(self) -> Dict[Tuple[int, int, int], Tuple[int, int, int]]:
		"""Get all blocks in the world."""
		return self.blocks.copy()


class SeededRandom:
	"""A seedable random number generator wrapper."""
	
	def __init__(self, seed: Optional[int] = None):
		if seed is None:
			seed = random.randint(0, 2**31 - 1)
		self.seed = seed
		self.rng = random.Random(seed)
	
	def reseed(self, seed: Optional[int] = None):
		"""Reseed the random number generator."""
		if seed is None:
			seed = random.randint(0, 2**31 - 1)
		self.seed = seed
		self.rng = random.Random(seed)
	
	def random(self) -> float:
		"""Return a random float in [0.0, 1.0)."""
		return self.rng.random()
	
	def randint(self, a: int, b: int) -> int:
		"""Return a random integer in [a, b]."""
		return self.rng.randint(a, b)
	
	def choice(self, seq):
		"""Return a random element from the sequence."""
		return self.rng.choice(seq)
	
	def shuffle(self, seq):
		"""Shuffle the sequence in place."""
		self.rng.shuffle(seq)


def visualize_world(world: World, title: str = "Minecraft Feature Visualization", 
				   show_axes: bool = False):
	"""
	Visualize the world in 3D using matplotlib with full unit cubes.
	
	Args:
		world: The World object to visualize
		title: Title for the plot
		show_axes: Whether to show the axes (default: False)
	"""
	if len(world.blocks) == 0:
		print("No blocks to visualize!")
		return None, None
	
	fig = plt.figure(figsize=(10, 8))
	ax = fig.add_subplot(111, projection='3d')
	
	# Extract coordinates and colors
	coords = list(world.blocks.keys())
	colors = list(world.blocks.values())
	
	if len(coords) == 0:
		return None, None
	
	x_coords = [c[0] for c in coords]
	y_coords = [c[1] for c in coords]
	z_coords = [c[2] for c in coords]
	
	# Find bounds
	min_x, max_x = min(x_coords), max(x_coords)
	min_y, max_y = min(y_coords), max(y_coords)
	min_z, max_z = min(z_coords), max(z_coords)
	
	# Create voxel arrays (add 1 to include the max coordinate)
	size_x = max_x - min_x + 1
	size_y = max_y - min_y + 1
	size_z = max_z - min_z + 1
	
	# Create boolean array for voxels and color array
	voxels = np.zeros((size_x, size_y, size_z), dtype=bool)
	voxel_colors = np.zeros((size_x, size_y, size_z, 3), dtype=float)
	
	# Fill in the voxels
	for (x, y, z), (r, g, b) in zip(coords, colors):
		# Convert to array indices
		idx_x = x - min_x
		idx_y = y - min_y
		idx_z = z - min_z
		
		voxels[idx_x, idx_y, idx_z] = True
		# Normalize RGB to [0, 1]
		voxel_colors[idx_x, idx_y, idx_z] = (r/255.0, g/255.0, b/255.0)
	
	# Draw cubes manually for precise positioning
	# Note: We map World (X, Y, Z) to Matplotlib (X, Z, Y) for proper orientation:
	# World X (left/right) -> Matplotlib X (left/right)
	# World Y (up/down) -> Matplotlib Z (up/down)
	# World Z (front/back) -> Matplotlib Y (front/back)
	def draw_cube(ax, world_x, world_y, world_z, color, size=1.0):
		"""Draw a cube at position (world_x, world_y, world_z) with given color.
		
		Args:
			world_x: World X coordinate (left/right)
			world_y: World Y coordinate (up/down)
			world_z: World Z coordinate (front/back)
		"""
		# Map to matplotlib coordinates: (x, z, y) -> (x, y, z) in matplotlib
		# matplotlib x = world_x, matplotlib y = world_z, matplotlib z = world_y
		x, y, z = world_x, world_z, world_y
		
		# Define the 8 vertices of a cube
		# Cube centered at (x, y, z) with size 1
		s = size / 2.0
		vertices = np.array([
			[x-s, y-s, z-s], [x+s, y-s, z-s], [x+s, y+s, z-s], [x-s, y+s, z-s],  # bottom
			[x-s, y-s, z+s], [x+s, y-s, z+s], [x+s, y+s, z+s], [x-s, y+s, z+s]   # top
		])
		
		# Define the 6 faces of the cube
		faces = [
			[vertices[0], vertices[1], vertices[2], vertices[3]],  # bottom
			[vertices[4], vertices[5], vertices[6], vertices[7]],  # top
			[vertices[0], vertices[1], vertices[5], vertices[4]],  # front
			[vertices[2], vertices[3], vertices[7], vertices[6]],  # back
			[vertices[1], vertices[2], vertices[6], vertices[5]],  # right
			[vertices[0], vertices[3], vertices[7], vertices[4]]   # left
		]
		
		# Create Poly3DCollection
		cube = Poly3DCollection(faces, facecolors=color, edgecolors='black', linewidths=0.1, alpha=1.0)
		ax.add_collection3d(cube)
	
	# Draw each block as a cube
	for (x, y, z), (r, g, b) in zip(coords, colors):
		color = (r/255.0, g/255.0, b/255.0)
		draw_cube(ax, x, y, z, color)
	
	# Set axis limits (mapped: X stays X, Y->Z, Z->Y)
	padding = 0.5
	ax.set_xlim(min_x - padding, max_x + 1 + padding)
	ax.set_ylim(min_z - padding, max_z + 1 + padding)  # World Z -> Matplotlib Y
	ax.set_zlim(min_y - padding, max_y + 1 + padding)  # World Y -> Matplotlib Z
	
	# Configure axes
	if show_axes:
		ax.set_xlabel('X')
		ax.set_ylabel('Z')  # World Z is front/back
		ax.set_zlabel('Y')  # World Y is up/down
	else:
		ax.set_axis_off()
	
	ax.set_title(title)
	
	# Set equal aspect ratio and bounds
	max_range = max(size_x, size_y, size_z) / 2.0
	if max_range == 0:
		max_range = 1
	mid_x = (min_x + max_x) * 0.5
	mid_y = (min_y + max_y) * 0.5
	mid_z = (min_z + max_z) * 0.5
	
	# Add some padding (mapped coordinates)
	padding = 0.5
	ax.set_xlim(mid_x - max_range - padding, mid_x + max_range + padding)
	ax.set_ylim(mid_z - max_range - padding, mid_z + max_range + padding)  # World Z -> Matplotlib Y
	ax.set_zlim(mid_y - max_range - padding, mid_y + max_range + padding)  # World Y -> Matplotlib Z
	
	# Enable free-panning (matplotlib 3D already supports this, but ensure it's enabled)
	ax.mouse_init()
	
	plt.tight_layout()
	return fig, ax


def run_interactive(feature_func: Callable[[World, SeededRandom], None], 
				   initial_seed: Optional[int] = None, show_axes: bool = False):
	"""
	Run an interactive visualization with keyboard controls.
	
	Args:
		feature_func: Function that takes (world, random_source) and generates a feature
		initial_seed: Optional initial seed (will be random if not provided)
		show_axes: Whether to show the axes (default: False)
	"""
	world = World()
	rng = SeededRandom(initial_seed)
	
	def regenerate():
		"""Regenerate the feature with a new seed."""
		nonlocal world, rng
		world.clear()
		rng.reseed()  # Generate new random seed
		feature_func(world, rng)
		print(f"Regenerated with seed: {rng.seed} ({len(world.blocks)} blocks)")
		return world, rng.seed
	
	def draw_cube(ax, world_x, world_y, world_z, color, size=1.0):
		"""Draw a cube at position (world_x, world_y, world_z) with given color.
		
		Args:
			world_x: World X coordinate (left/right)
			world_y: World Y coordinate (up/down)
			world_z: World Z coordinate (front/back)
		"""
		# Map to matplotlib coordinates: (x, z, y) -> (x, y, z) in matplotlib
		# matplotlib x = world_x, matplotlib y = world_z, matplotlib z = world_y
		x, y, z = world_x, world_z, world_y
		
		s = size / 2.0
		vertices = np.array([
			[x-s, y-s, z-s], [x+s, y-s, z-s], [x+s, y+s, z-s], [x-s, y+s, z-s],  # bottom
			[x-s, y-s, z+s], [x+s, y-s, z+s], [x+s, y+s, z+s], [x-s, y+s, z+s]   # top
		])
		
		faces = [
			[vertices[0], vertices[1], vertices[2], vertices[3]],  # bottom
			[vertices[4], vertices[5], vertices[6], vertices[7]],  # top
			[vertices[0], vertices[1], vertices[5], vertices[4]],  # front
			[vertices[2], vertices[3], vertices[7], vertices[6]],  # back
			[vertices[1], vertices[2], vertices[6], vertices[5]],  # right
			[vertices[0], vertices[3], vertices[7], vertices[4]]   # left
		]
		
		cube = Poly3DCollection(faces, facecolors=color, edgecolors='black', linewidths=0.1, alpha=1.0)
		ax.add_collection3d(cube)
	
	def redraw():
		"""Redraw the visualization."""
		ax.clear()
		if len(world.blocks) > 0:
			coords = list(world.blocks.keys())
			colors = list(world.blocks.values())
			
			x_coords = [c[0] for c in coords]
			y_coords = [c[1] for c in coords]
			z_coords = [c[2] for c in coords]
			
			# Find bounds
			min_x, max_x = min(x_coords), max(x_coords)
			min_y, max_y = min(y_coords), max(y_coords)
			min_z, max_z = min(z_coords), max(z_coords)
			
			# Draw each block as a cube
			for (x, y, z), (r, g, b) in zip(coords, colors):
				color = (r/255.0, g/255.0, b/255.0)
				draw_cube(ax, x, y, z, color)
			
			# Configure axes
			if show_axes:
				ax.set_xlabel('X')
				ax.set_ylabel('Z')  # World Z is front/back
				ax.set_zlabel('Y')  # World Y is up/down
			else:
				ax.set_axis_off()
			
			ax.set_title(f"Minecraft Feature Visualization (Seed: {rng.seed})")
			
			# Set axis limits (mapped: X stays X, Y->Z, Z->Y)
			padding = 0.5
			ax.set_xlim(min_x - padding, max_x + 1 + padding)
			ax.set_ylim(min_z - padding, max_z + 1 + padding)  # World Z -> Matplotlib Y
			ax.set_zlim(min_y - padding, max_y + 1 + padding)  # World Y -> Matplotlib Z
			
			ax.mouse_init()
		
		fig.canvas.draw()
	
	def on_key(event):
		"""Handle keyboard events."""
		if event.key == 'r' or event.key == 'R':
			regenerate()
			redraw()
	
	# Initial generation
	feature_func(world, rng)
	print(f"Initial generation with seed: {rng.seed} ({len(world.blocks)} blocks)")
	print("Controls:")
	print("  - Mouse: Rotate and pan the 3D view (free-panning enabled)")
	print("  - R key: Regenerate with a new random seed")
	print("  - Close window to exit")
	if show_axes:
		print("  - Axes are visible (set show_axes=False to hide)")
	
	# Create visualization
	fig, ax = visualize_world(world, f"Minecraft Feature Visualization (Seed: {rng.seed})", 
							 show_axes=show_axes)
	
	if fig is None:
		return
	
	# Connect keyboard event
	fig.canvas.mpl_connect('key_press_event', on_key)
	
	# Show the plot (this blocks until window is closed)
	plt.show()


# Example feature generation function
def example_feature(world: World, random_source: SeededRandom):
	"""
	Example feature that generates a simple structure.
	This is a placeholder - replace with your actual feature logic.
	"""
	# Generate a simple 5x5x5 cube
	for x in range(-2, 3):
		for y in range(0, 5):
			for z in range(-2, 3):
				# Use random colors for variety
				r = random_source.randint(100, 255)
				g = random_source.randint(100, 255)
				b = random_source.randint(100, 255)
				world.set_block((x, y, z), (r, g, b))


if __name__ == "__main__":
	# Run interactive visualization
	# Replace example_feature with your own feature function
	run_interactive(example_feature, 0, False)

