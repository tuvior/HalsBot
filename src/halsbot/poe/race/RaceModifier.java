package halsbot.poe.race;

public enum RaceModifier {
    Ancestral {
        @Override
        public String getDescription() {
            return "All areas except in towns have enemy totems spread across the zone that grant benefits to your enemies and attempt to kill you.";
        }

        @Override
        public String toString() {
            return "Ancestral";
        }
    },
    Blood_Grip {
        @Override
        public String getDescription() {
            return "Players have the Corrupting Blood mod.";
        }

        @Override
        public String toString() {
            return "Blood Grip";
        }
    },
    Blood_Magic {
        @Override
        public String getDescription() {
            return "Characters act as if they have the Blood Magic passive allocated.";
        }

        @Override
        public String toString() {
            return "Blood Magic";
        }
    },
    Boss_Kill {
        @Override
        public String getDescription() {
            return "Points are awarded for time taken to kill a specific boss. Bosses used include Brutus, Merveil, Oversoul, Piety and Dominus.";
        }

        @Override
        public String toString() {
            return "Boss Kill";
        }
    },
    Brutal {
        @Override
        public String getDescription() {
            return "Monsters attack and cast 20% faster and deal 20% more damage.";
        }

        @Override
        public String toString() {
            return "Brutal";
        }
    },
    Burst {
        @Override
        public String getDescription() {
            return "3 short races in a row and all within 1 hour.";
        }

        @Override
        public String toString() {
            return "Burst";
        }
    },
    Cut_throat {
        @Override
        public String getDescription() {
            return "-Equipped items are dropped on death. " +
                    "-Players are allowed to enter others' instances at will. " +
                    "-Killing a player awards their death penalty experience to the killer. " +
                    "-Player capacity in non-town instances is doubled. This does not increase the maximum party size.";
        }

        @Override
        public String toString() {
            return "Cut-throat";
        }
    },
    Emberwake {
        @Override
        public String getDescription() {
            return "-Areas have the same layout for all players " +
                    "-Minimap is revealed " +
                    "-30% increased movement speed " +
                    "-Monsters deal 30% extra damage as fire";
        }

        @Override
        public String toString() {
            return "Emberwake";
        }
    },
    Eternal_Torment {
        @Override
        public String getDescription() {
            return "-Each area is inhabited by 10 additional Tormented Spirits (except for Twilight Strand and towns). " +
                    "-Spirit spawns ignore the usual area level restriction.";
        }

        @Override
        public String toString() {
            return "Eternal Torment";
        }
    },
    Exiles_Everywhere {
        @Override
        public String getDescription() {
            return "-Each area is inhabited by 20 additional Rogue Exiles (except for Twilight Strand, where Rogue Exiles cannot spawn) " +
                    "-Rogue Exile spawns ignore the usual area level restriction, e.g. it's possible to encounter Xandro Bloddrinker in Mud Flats";
        }

        @Override
        public String toString() {
            return "Exiles Everywhere";
        }
    },
    Famine {
        @Override
        public String getDescription() {
            return "Flasks, life, mana, and energy shield are not refilled when entering a town instance.";
        }

        @Override
        public String toString() {
            return "Famine";
        }
    },
    Fixed_Seed {
        @Override
        public String getDescription() {
            return "-All instances of a given area will have the same area layout, for all players. " +
                    "-The minimap will automatically be fully revealed. " +
                    "-Multiple Race Events regularly will have the same seed. That is, the instance area layouts will be same in all the races of a fixed seed set.";
        }

        @Override
        public String toString() {
            return "Fixed Seed";
        }
    },
    Fracturing {
        @Override
        public String getDescription() {
            return "All non-unique monsters will split into a several copies when they are killed.";
        }

        @Override
        public String toString() {
            return "Fracturing";
        }
    },
    Headhunter {
        @Override
        public String getDescription() {
            return "When you kill a rare monster, you gain all its mods for 20 seconds. Every monster pack has a rare monster (even the Twilight Strand). This race type is voided.";
        }

        @Override
        public String toString() {
            return "Headhunter";
        }
    },
    Immolation {
        @Override
        public String getDescription() {
            return "Players and monsters have their physical damage converted to fire damage. Monsters will create ground fire on death.";
        }

        @Override
        public String toString() {
            return "Immolation";
        }
    },
    Inferno {
        @Override
        public String getDescription() {
            return "All areas are affected by the the Infernal Tempest.";
        }

        @Override
        public String toString() {
            return "Inferno";
        }
    },
    Lethal {
        @Override
        public String getDescription() {
            return "Monsters deal 50% increased damage. An additional 50% of that damage is then added as cold damage, fire damage and lightning damage.";
        }

        @Override
        public String toString() {
            return "Lethal";
        }
    },
    Multiple_Projectile {
        @Override
        public String getDescription() {
            return "Monsters have four additional projectiles when using projectile-based skills and attacks.";
        }

        @Override
        public String toString() {
            return "Multiple Projectile";
        }
    },
    No_Projectiles {
        @Override
        public String getDescription() {
            return "Players deal no damage with projectile-based skills and attacks.";
        }

        @Override
        public String toString() {
            return "No Projectiles";
        }
    },
    Rogue {
        @Override
        public String getDescription() {
            return "All areas except towns have a rogue exile, which spawns at a random location within the area.";
        }

        @Override
        public String toString() {
            return "Rogue";
        }
    },
    Solo {
        @Override
        public String getDescription() {
            return "Players cannot join parties or trade.";
        }

        @Override
        public String toString() {
            return "Solo";
        }
    },
    Soulthirst {
        @Override
        public String getDescription() {
            return "You gain Soul Eater for 10 seconds upon killing a Rare monster.";
        }

        @Override
        public String toString() {
            return "Soulthirst";
        }
    },
    Turbo {
        @Override
        public String getDescription() {
            return "Monsters run, attack and cast 60% faster.";
        }

        @Override
        public String toString() {
            return "Turbo";
        }
    },
    Unwavering {
        @Override
        public String getDescription() {
            return "Monsters have increased life and cannot be stunned.";
        }

        @Override
        public String toString() {
            return "Unwavering";
        }
    };

    public abstract String getDescription();
}
