package com.gm910.petgodmod.gods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class NameBuilder {

	public static abstract class Letter {
		/**
		 * Name of this letter
		 */
		public final String l;

		public Letter(String l) {
			this.l = l;
		}

		public boolean isSolitary() {
			return false;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return l;
		}

		/**
		 * n == cannot follow<br>
		 * v == can follow but the resultant consonant cluster must be followed by a
		 * vowel y == can follow
		 * 
		 * @param previous
		 * @param previousprevious
		 * @return
		 */
		public abstract char canFollow(List<Letter> name, Letter previous, Letter previousprevious);

		public boolean isConsonant() {
			return this instanceof Con;
		}

		public boolean isVowel() {
			return this instanceof Vow;
		}
	}

	public static class Con extends Letter {

		public static final List<Con> CONSONANTS = ImmutableList.copyOf(Lists.newArrayList(of("b", 0, 1, 0),
				of("c", 1, 1, 0), of("d", 0, 1, 0), of("f", 1, 1, 0), of("g", 0, 1, 0), of("h", 1, 1, 1),
				of("j", 0, 0, 0, 1), of("k", 1, 1, 0), of("l", 1, 0, 1), of("m", 1, 0, 0), of("n", 1, 0, 0),
				of("p", 1, 1, 0), of("q", 1, 1, 0), of("r", 0, 0, 1), of("s", 1, 1, 0), of("t", 1, 1, 0),
				of("th", 0, 1, 0), of("v", 0, 1, 0), of("w", 1, 0, 1), of("x", 0, 0, 0, 1), of("z", 0, 0, 0, 1),
				of("'", 0, 0, 0, 1), new Con("y", false, false, false) {
					@Override
					public char canFollow(List<Letter> name, Letter previous, Letter pp) {
						char d = super.canFollow(name, previous, pp);
						if (d != 'n') {
							return previous == null ? d : previous.l.equals(this.l) ? 'n' : d;
						}
						return d;
					}
				}));

		/**
		 * Whether this can be the center of a triple consonant cluster basically
		 */
		public final boolean canBePrecededByS;
		public final boolean precedes;
		public final boolean follows;

		private Con(String name, boolean cbpbs, boolean p, boolean f) {
			super(name);
			this.canBePrecededByS = cbpbs;
			this.precedes = p;
			this.follows = f;
		}

		public static Con of(String name, int cs, int p, int f) {
			return new Con(name, cs != 0, p != 0, f != 0);
		}

		public static Con of(String name, int cs, int p, int f, int s) {
			return s == 0 ? of(name, cs, p, f) : new Con(name, cs != 0, p != 0, f != 0) {
				@Override
				public boolean isSolitary() {
					return true;
				}
			};
		}

		@Override
		public char canFollow(List<Letter> name, Letter previous, Letter pp) {
			if (previous instanceof Con && (previous.isSolitary() || this.isSolitary())) {
				return 'n';
			}
			if (pp == this && previous == this)
				return 'n';
			if (previous == null || previous instanceof Vow) {
				return this.precedes ? 'y' : 'v';
			} else {
				Con prev = (Con) previous;
				if (pp instanceof Vow) {

					if (this.follows) {
						return 'v';
					} else {
						return 'y';
					}

				} else if (pp instanceof Con) {

					return prev.precedes && this.follows ? 'v' : 'n';
				} else {
					if (prev.precedes && this.follows) {
						return 'v';
					} else if (prev.l.equals("s") && this.canBePrecededByS) {
						return this.precedes ? 'y' : 'v';
					} else {
						return 'n';
					}
				}
			}
		}

	}

	public static class Vow extends Letter {

		public static class ForeignVow extends Vow {

			public ForeignVow(String l, int r) {
				super(l, r);
			}

			@Override
			public boolean isSolitary() {
				return true;
			}

			@Override
			public char canFollow(List<Letter> name, Letter previous, Letter pp) {
				char d = super.canFollow(name, previous, pp);
				if (d != 'n') {
					List<Letter> co = Lists.newArrayList(name);
					co.removeIf((e) -> !(e instanceof ForeignVow));
					List<Letter> cop = Lists.newArrayList(name);
					cop.removeIf((e) -> !(e.l == this.l));
					if (co.size() > 1 || cop.size() > 0) {
						return 'n';
					}

				}
				return d;
			}
		}

		public static final List<Vow> VOWELS = ImmutableList.copyOf(Lists.newArrayList(new Vow("a", 1), new Vow("e", 1),
				new Vow("i", 0), new Vow("o", 1), new Vow("u", 0), new Vow("y", 0) {
					@Override
					public char canFollow(List<Letter> name, Letter previous, Letter pp) {
						char d = super.canFollow(name, previous, pp);
						if (d != 'n') {
							return previous == null ? d : previous.l.equals(this.l) ? 'n' : d;
						}
						return d;
					}
				}, new ForeignVow("\u00E9", 0), new ForeignVow("\u00E1", 0), new ForeignVow("\u00ED", 0),
				new ForeignVow("\u00FA", 0), new ForeignVow("\u00F3", 0), new ForeignVow("\u00E6", 0)));

		public final boolean repeatable;

		public Vow(String l, int r) {
			super(l);
			this.repeatable = r != 0;
		}

		@Override
		public char canFollow(List<Letter> name, Letter previous, Letter pp) {
			if (previous instanceof Vow && (((Vow) previous).isSolitary() || this.isSolitary())) {
				return 'n';
			}
			if ((name.size() >= 3 && name.get(name.size() - 3) instanceof Vow) && previous instanceof Vow
					&& pp instanceof Vow) {
				return 'n';
			}
			if (!repeatable) {
				if (previous == this) {
					return 'n';
				} else {
					return 'y';
				}
			} else {
				return (pp == this && previous == this) ? 'n' : 'y';
			}
		}

	}

	public static Con randomCon(Random rand) {
		return Con.CONSONANTS.stream().sorted((e, e2) -> rand.nextInt(2) - 1).findFirst().get();
	}

	public static Vow randomVow(Random rand) {
		return Vow.VOWELS.stream().sorted((e, e2) -> rand.nextInt(2) - 1).findFirst().get();
	}

	public static String genName(@Nullable Random rand) {
		return genName(7, rand);
	}

	public static String genName(int maxLength, Random rand) {
		if (maxLength < 2) {
			throw new RuntimeException(maxLength + " is too short");
		}
		if (rand == null)
			rand = new Random();
		List<Letter> name = new ArrayList<>();
		boolean vow = rand.nextBoolean();
		char lastResult = 'y';
		Letter prev = null;
		Letter pp = null;
		main: for (int i = 0; i < maxLength || lastResult == 'v' || new ArrayList<>(name).size() < 3; i++) {
			Letter toAdd = null;
			if (i == 0) {
				if (vow) {
					toAdd = randomVow(rand);
				} else {
					toAdd = randomCon(rand);

				}
			} else {
				vow = rand.nextBoolean();
				if (lastResult == 'v' || vow) {
					int thresh = 0;
					while (toAdd == null || toAdd.canFollow(name, prev, pp) == 'n') {
						toAdd = randomVow(rand);
						thresh++;
						if (thresh > Vow.VOWELS.size()) {
							if (lastResult == 'v') {
								lastResult = 'y';
							}
							continue main;
						}
					}
				} else {
					int thresh = 0;
					while (toAdd == null || toAdd.canFollow(name, prev, pp) == 'n') {
						toAdd = randomCon(rand);
						thresh++;
						if (thresh > Con.CONSONANTS.size()) {
							continue main;
						}
					}
				}
			}
			lastResult = toAdd.canFollow(name, prev, pp);
			name.add(toAdd);
			System.out.println(String.join("", name.stream().map((e) -> e.l).toList()));
			pp = prev;
			prev = toAdd;
		}

		String namestr = String.join("", name.stream().map((e) -> e.l).toList());

		if (Character.isAlphabetic(namestr.charAt(0))) {
			return namestr.substring(0, 1).toUpperCase() + namestr.substring(1);
		} else {

			return namestr.substring(0, 1) + namestr.substring(1, 2).toUpperCase() + namestr.substring(2);
		}
	}

}
