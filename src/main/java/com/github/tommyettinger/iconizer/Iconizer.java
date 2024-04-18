package com.github.tommyettinger.iconizer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Loads an OpenMoji atlas when created, then allows icons to be generated with {@link #generate(int, int, long)}.
 * The icons are randomized Pixmaps showing two OpenMoji icons (one on the left half, one on the right). The colors are
 * all randomized. This renders to a framebuffer, so it uses OpenGL and as such cannot be used on the headless backend.
 * You can only create an Iconizer after {@link ApplicationListener#create()} has been called by the framework.
 */
public final class Iconizer {

    public static final String[] regions = {
            "1st place medal.png", "2nd place medal.png", "3rd place medal.png",
            "A button (blood type).png", "AB button (blood type).png", "abacus.png",
            "accordion.png", "add button.png", "add contact.png", "adhesive bandage.png",
            "admission tickets.png", "aerial tramway.png", "airplane arrival.png",
            "airplane departure.png", "airplane.png", "alarm clock.png", "alembic.png",
            "alien monster.png", "alien.png", "ambulance.png", "american football.png",
            "amphora.png", "anatomical heart.png", "anchor.png", "anger symbol.png",
            "angry face with horns.png", "angry face.png", "anguished face.png",
            "annoyed face with tongue.png", "ant.png", "antenna bars.png",
            "anticlockwise triangle-headed top u-shaped arrow.png",
            "anxious face with sweat.png", "Aquarius.png", "archive.png", "arduino.png",
            "Aries.png", "armchair.png", "arrow turn right.png", "articulated lorry.png",
            "artist palette.png", "artist.png", "assembly group.png", "assembly point.png",
            "astonished face.png", "astronaut.png", "ATM sign.png", "atom bomb.png",
            "atom symbol.png", "augmented reality.png", "authority building.png",
            "authority instruction.png", "authority.png", "auto rickshaw.png",
            "automobile.png", "autonomous car.png", "avalanche.png", "avocado.png",
            "axe.png", "B button (blood type).png", "baby angel.png", "baby bottle.png",
            "baby chick.png", "baby symbol.png", "baby.png", "BACK arrow.png",
            "backache.png", "backhand index pointing down.png",
            "backhand index pointing left.png", "backhand index pointing right.png",
            "backhand index pointing up.png", "backpack.png", "bacon.png", "badger.png",
            "badminton.png", "bagel.png", "baggage claim.png", "baguette bread.png",
            "balance scale.png", "ballet shoes.png", "balloon.png",
            "ballot box with ballot.png", "banana.png", "bandage change.png",
            "bandage scissors.png", "banjo.png", "bank.png", "bar chart.png",
            "barber pole.png", "barcode.png", "barista.png", "baseball.png", "basket.png",
            "basketball.png", "bat.png", "bathtub.png", "battery.png",
            "beach with umbrella.png", "beaming face with smiling eyes.png", "beans.png",
            "bear.png", "beating heart.png", "beaver.png", "bed linen.png", "bed.png",
            "beetle.png", "bell pepper.png", "bell with slash.png", "bell.png",
            "bellhop bell.png", "beluga.png", "bento box.png", "beverage box.png",
            "bicycle.png", "bikini.png", "billed cap.png", "biohazard.png", "bird.png",
            "birthday cake.png", "bison.png", "biting lip.png", "black bird.png",
            "black cat.png", "black circle.png", "black heart.png", "black hexagon.png",
            "black hole.png", "black large circle.png", "black large square.png",
            "black medium-small square.png", "black medium square.png", "black nib.png",
            "black octagon.png", "black pentagon.png", "black rectangle.png",
            "black small square.png", "black square button.png", "black star.png",
            "black vertical ellipse.png", "black vertical rectangle.png",
            "blood transfusion.png", "blossom.png", "blowfish.png", "blue book.png",
            "blue circle.png", "blue heart.png", "blue hexagon.png", "blue square.png",
            "blueberries.png", "boar.png", "bone.png", "bookmark tabs.png", "bookmark.png",
            "books.png", "boomerang.png", "boule bread.png", "bouquet.png",
            "bow and arrow.png", "bowl with spoon.png", "bowling.png", "boxing glove.png",
            "boy.png", "brain.png", "bread.png", "brick.png", "bridge at night.png",
            "briefcase.png", "briefs.png", "bright button.png", "broccoli.png",
            "broken heart.png", "broom.png", "brown circle.png", "brown heart.png",
            "brown hexagon.png", "brown square.png", "bubble tea.png", "bubbles.png",
            "bucket.png", "bug.png", "building construction.png", "bullet train.png",
            "bullseye.png", "burrito.png", "bus stop.png", "bus.png",
            "bust in silhouette.png", "busts in silhouette.png", "butter.png",
            "butterfly.png", "cable.png", "cactus.png", "cafeteria.png", "cake.png",
            "calendar.png", "call me hand.png", "camel.png", "camera with flash.png",
            "camera.png", "camping.png", "Cancer.png", "candle.png", "candy.png",
            "canned food.png", "canoe.png", "Capricorn.png", "card file box.png",
            "card index dividers.png", "card index.png", "carousel horse.png",
            "carp streamer.png", "carpentry saw.png", "carrot.png", "castle.png",
            "cat face.png", "cat with tears of joy.png", "cat with wry smile.png",
            "cat.png", "chains.png", "chair.png", "champignon brown.png",
            "champignon white.png", "charge plug.png", "chart decreasing.png",
            "chart increasing with yen.png", "chart increasing.png", "chats.png",
            "check box with check.png", "check mark button.png", "check mark.png",
            "cheese wedge.png", "cherries.png", "cherry blossom.png", "chess pawn.png",
            "chestnut.png", "chicken.png", "child.png", "children crossing.png",
            "chipmunk.png", "chocolate bar.png", "chopsticks.png", "Christmas tree.png",
            "cinema.png", "circle with left half black.png",
            "circle with right half black.png", "circled anticlockwise arrow.png",
            "circled c with overlaid backslash.png", "circled cc.png",
            "circled dollar sign with overlaid backslash.png", "circled equals.png",
            "circled human figure.png", "circled M.png", "circled zero with slash.png",
            "circuit.png", "circus tent.png", "cityscape at dusk.png", "cityscape.png",
            "CL button.png", "clamp.png", "clapper board.png", "clapping hands.png",
            "classical building.png", "clinical thermometer.png", "clipboard.png",
            "clockwise vertical arrows.png", "close.png", "closed book.png",
            "closed umbrella.png", "cloud with lightning and rain.png",
            "cloud with lightning.png", "cloud with rain.png", "cloud with snow.png",
            "cloud.png", "clown face.png", "club suit.png", "clutch bag.png", "coat.png",
            "cockroach.png", "coconut.png", "code editor.png", "coffee grinder.png",
            "coffin.png", "coin.png", "cold face.png", "collaboration.png", "collision.png",
            "colossus of rhodes.png", "comet.png", "comment.png", "compass.png",
            "compose.png", "computer disk.png", "computer mouse.png", "confetti ball.png",
            "confounded face.png", "confused face.png", "construction worker.png",
            "construction.png", "contacts.png", "control knobs.png",
            "convenience store.png", "cook.png", "cooked rice.png", "cookie.png",
            "cooking.png", "COOL button.png", "copy.png", "copyleft symbol.png",
            "copyright.png", "coral.png", "couch and lamp.png",
            "counterclockwise arrows button.png", "cow face.png", "cow.png",
            "cowboy hat face.png", "crab.png", "crayon.png", "credit card.png",
            "crescent moon.png", "cricket game.png", "cricket.png", "crocodile.png",
            "croissant.png", "cross mark button.png", "cross mark.png",
            "crossed fingers.png", "crossed swords.png", "crown.png", "crutch.png",
            "crutches.png", "crying cat.png", "crying face.png", "crystal ball.png",
            "ct scan.png", "cucumber.png", "cup with straw.png", "cupcake.png",
            "curling stone.png", "curly loop.png", "currency exchange.png",
            "curry rice.png", "cursor.png", "custard.png", "customs.png", "cut of meat.png",
            "cyclone.png", "dagger.png", "dango.png", "dashing away.png", "deaf man.png",
            "deaf person.png", "deaf woman.png", "deciduous tree.png", "deer.png",
            "dejected face.png", "delete.png", "delivery truck.png", "department store.png",
            "derelict house.png", "desert island.png", "desert.png", "desktop computer.png",
            "details.png", "detective.png", "diamond suit.png", "diamond with a dot.png",
            "dim button.png", "disappointed face.png", "disguised face.png",
            "disinfect surface.png", "divide.png", "diving mask.png", "diya lamp.png",
            "dizzy.png", "dj man.png", "dj woman.png", "dj.png", "dna.png", "dodo.png",
            "doe.png", "dog face.png", "dog.png", "dollar banknote.png", "dolphin.png",
            "donkey.png", "door.png", "dotted line face.png", "double curly loop.png",
            "double exclamation mark.png", "double tap.png", "doughnut.png", "dove.png",
            "down-left arrow.png", "down-right arrow.png", "down arrow.png",
            "downcast face with sweat.png", "download.png", "downwards button.png",
            "dragon face.png", "dragon.png", "dress.png", "drip coffee maker.png",
            "drone.png", "drooling face.png", "drop cover hold.png", "drop of blood.png",
            "droplet.png", "drum.png", "duck.png", "dumpling.png", "duplicate.png",
            "dvd.png", "e-mail.png", "eagle.png", "ear of corn.png",
            "ear with hearing aid.png", "ear.png", "earache.png", "earthquake.png",
            "ecg waves.png", "edit.png", "egg.png", "eiffel tower.png",
            "eight-pointed star.png", "eight-spoked asterisk.png", "eight-thirty.png",
            "eight o'clock.png", "eject button.png", "electric coffee percolator.png",
            "electric plug red.png", "electric plug.png", "elephant.png", "elevator.png",
            "eleven-thirty.png", "eleven o'clock.png", "elf.png", "emergency exit door.png",
            "emergency exit.png", "empty nest.png", "END arrow.png", "enraged face.png",
            "envelope with arrow.png", "envelope.png", "espresso machine.png",
            "euro banknote.png", "european name badge.png", "evacuate downstairs.png",
            "evacuate fire.png", "evacuate to shelter.png", "evacuate vertical.png",
            "evacuate.png", "evergreen tree.png", "ewe.png",
            "exclamation question mark.png", "exhaust gases car.png",
            "exhaust gases factory.png", "exhausted face.png", "exit.png",
            "exploding head.png", "expressionless face.png", "eye in speech bubble.png",
            "eye pain.png", "eye.png", "eyes.png", "face blowing a kiss.png",
            "face exhaling.png", "face holding back tears.png", "face in clouds.png",
            "face savoring food.png", "face screaming in fear.png", "face vomiting.png",
            "face with crossed-out eyes.png", "face with diagonal mouth.png",
            "face with hand over mouth.png", "face with head-bandage.png",
            "face with medical mask.png", "face with monocle.png",
            "face with open eyes and hand over mouth.png", "face with open mouth.png",
            "face with peeking eye.png", "face with raised eyebrow.png",
            "face with rolling eyes.png", "face with spiral eyes.png",
            "face with steam from nose.png", "face with symbols on mouth.png",
            "face with tears of joy.png", "face with thermometer.png",
            "face with tongue.png", "face without mouth.png", "factory worker.png",
            "factory.png", "fairy.png", "falafel.png", "fallen leaf.png", "farmer.png",
            "fast-forward button.png", "fast down button.png", "fast reverse button.png",
            "fast up button.png", "fax machine.png", "fearful face.png", "feather.png",
            "female doctor.png", "female nurse.png", "female sign.png", "ferris wheel.png",
            "ferry.png", "field hockey.png", "file cabinet.png", "file folder.png",
            "film frames.png", "film projector.png", "filter.png",
            "finger pushing button.png", "fire engine.png", "fire extinguisher.png",
            "fire.png", "firecracker.png", "firefighter.png", "fireworks.png",
            "first aid bag.png", "first aid kit.png", "first aid.png",
            "first quarter moon face.png", "first quarter moon.png",
            "fish cake with swirl.png", "fish.png", "fisheye.png", "fishing pole.png",
            "five-thirty.png", "five o'clock.png", "flamingo.png", "flashlight.png",
            "flat shoe.png", "flatbread.png", "fleur-de-lis.png", "flexed biceps.png",
            "floating ice broken.png", "floating ice.png", "flood.png", "floppy disk.png",
            "flower playing cards.png", "flushed face.png", "flute.png", "fly.png",
            "flying disc.png", "flying saucer.png", "fog.png", "foggy mountain.png",
            "foggy.png", "folded hands.png", "folding hand fan.png", "fondue.png",
            "foot.png", "footprints.png", "forceps.png", "fork and knife with plate.png",
            "fork and knife.png", "fortune cookie.png", "forward.png", "fountain pen.png",
            "fountain.png", "four-thirty.png", "four leaf clover.png", "four o'clock.png",
            "fox.png", "fracture leg.png", "framed picture.png", "FREE button.png",
            "french fries.png", "french press.png", "fried shrimp.png", "frog.png",
            "front-facing baby chick.png", "frowning face with open mouth.png",
            "frowning face.png", "fuel pump.png", "full moon face.png", "full moon.png",
            "funeral urn.png", "game die.png", "gardener man.png", "gardener woman.png",
            "gardening gloves.png", "garlic.png", "gear.png", "geiger counter.png",
            "gem stone.png", "Gemini.png", "genie.png", "ghost.png", "ginger root.png",
            "giraffe.png", "girl.png", "glass bottle.png", "glass of milk.png",
            "glasses.png", "globe showing Americas.png", "globe showing Asia-Australia.png",
            "globe showing Europe-Africa.png", "globe with meridians.png", "gloves.png",
            "glowing star.png", "goal net.png", "goat.png", "goblin.png", "goggles.png",
            "goldfish.png", "goose.png", "gorilla.png", "gps.png", "graduation cap.png",
            "grapes.png", "great pyramid of giza.png", "green apple.png", "green book.png",
            "green circle.png", "green heart.png", "green hexagon.png", "green salad.png",
            "green square.png", "grey heart.png", "grimacing face.png",
            "grinning cat with smiling eyes.png", "grinning cat.png",
            "grinning face with big eyes.png", "grinning face with smiling eyes.png",
            "grinning face with sweat.png", "grinning face.png",
            "grinning squinting face.png", "growing heart.png", "guard.png",
            "guide dog.png", "guitar.png", "guy fawkes mask.png", "hacker cat.png",
            "hair pick.png", "HAL 9000.png", "half orange fruit.png", "hamburger menu.png",
            "hamburger.png", "hammer and pick.png", "hammer and wrench.png", "hammer.png",
            "hamsa.png", "hamster.png", "hand with fingers splayed.png",
            "hand with index finger and thumb crossed.png", "handbag.png", "handshake.png",
            "hanging gardens of babylon.png", "hatching chick.png", "headache.png",
            "headphone.png", "headstone.png", "health worker.png",
            "hear-no-evil monkey.png", "heart decoration.png", "heart exclamation.png",
            "heart hands.png", "heart on fire.png", "heart suit.png",
            "heart with arrow.png", "heart with ribbon.png", "heavy circle.png",
            "heavy dollar sign.png", "heavy equals sign.png", "hedgehog.png",
            "helicopter.png", "help others.png", "herb.png", "hibiscus.png",
            "high-heeled shoe.png", "high-speed train.png", "high voltage.png",
            "hiking boot.png", "hippopotamus.png", "hold.png", "hole.png",
            "hollow red circle.png", "home button.png", "honey pot.png", "honeybee.png",
            "hook.png", "horizontal black hexagon.png", "horizontal black octagon.png",
            "horizontal traffic light.png", "horse face.png", "horse jumping hurdle.png",
            "horse racing.png", "horse riding.png", "horse.png", "hospital.png",
            "hot-water bottle.png", "hot beverage.png", "hot dog.png", "hot face.png",
            "hot pepper.png", "hot springs.png", "hotel.png", "hourglass done.png",
            "hourglass not done.png", "house with garden.png", "house.png", "houses.png",
            "hundred points.png", "hushed face.png", "hut.png", "hyacinth.png",
            "hyphen-minus.png", "ibeacon.png", "ice core sample.png", "ice cream.png",
            "ice hockey.png", "ice shelf melting.png", "ice shelf.png", "ice skate.png",
            "ice.png", "iceberg.png", "ID button.png", "identification card.png",
            "inbox tray.png", "inbox.png", "incoming envelope.png", "incredulous face.png",
            "index pointing at the viewer.png", "index pointing up.png", "infinity.png",
            "information.png", "input latin letters.png", "input latin lowercase.png",
            "input latin uppercase.png", "input numbers.png", "input symbols.png",
            "interview.png", "intestine.png", "intricate.png", "jack-o-lantern.png",
            "Japanese 'acceptable' button.png", "Japanese 'application' button.png",
            "Japanese 'bargain' button.png", "Japanese 'congratulations' button.png",
            "Japanese 'discount' button.png", "Japanese 'free of charge' button.png",
            "Japanese 'here' button.png", "Japanese 'monthly amount' button.png",
            "Japanese 'no vacancy' button.png", "Japanese 'not free of charge' button.png",
            "Japanese 'open for business' button.png",
            "Japanese 'passing grade' button.png", "Japanese 'prohibited' button.png",
            "Japanese 'reserved' button.png", "Japanese 'secret' button.png",
            "Japanese 'service charge' button.png", "Japanese 'vacancy' button.png",
            "Japanese castle.png", "Japanese dolls.png", "Japanese post office.png",
            "Japanese symbol for beginner.png", "jar with blue content.png",
            "jar with brown content.png", "jar with green content.png",
            "jar with orange content.png", "jar with purple content.png",
            "jar with red content.png", "jar with yellow content.png", "jar.png",
            "jeans.png", "jellyfish.png", "joint pain.png", "joker.png", "joystick.png",
            "judge.png", "kangaroo.png", "kehrwoche.png", "key.png", "keyboard.png",
            "keycap, 0.png", "keycap, 1.png", "keycap, 10.png", "keycap, 2.png",
            "keycap, 3.png", "keycap, 4.png", "keycap, 5.png", "keycap, 6.png",
            "keycap, 7.png", "keycap, 8.png", "keycap, 9.png", "keycap, asterisk.png",
            "keycap, number sign.png", "kick scooter.png", "kidney.png", "kimono.png",
            "kitchen knife.png", "kite.png", "kiwi fruit.png", "knee pain.png", "knot.png",
            "koala.png", "lab coat.png", "label.png", "lacrosse.png", "ladder.png",
            "lady beetle.png", "landslide.png", "laptop.png", "large blue diamond.png",
            "large intestine.png", "large orange diamond.png", "last quarter moon face.png",
            "last quarter moon.png", "last track button.png", "latte macchiato.png",
            "lawn mower.png", "leaf fluttering in wind.png", "leafy green.png", "led.png",
            "ledger.png", "left-facing fist.png", "left-right arrow.png",
            "left arrow curving right.png", "left arrow.png", "left luggage.png",
            "left right black arrow.png", "left speech bubble.png", "leftwards hand.png",
            "leftwards pushing hand.png", "leg.png", "lemon.png",
            "lentils with spaetzle.png", "Leo.png", "leopard.png", "level slider.png",
            "Libra.png", "light blue heart.png", "light bulb.png", "light rail.png",
            "lighter.png", "lighthouse of alexandria.png", "link.png",
            "linked paperclips.png", "lion.png", "lipstick.png", "litter in bin sign.png",
            "liver.png", "lizard.png", "llama.png", "lobster.png",
            "location indicator red.png", "location indicator.png", "locked with key.png",
            "locked with pen.png", "locked.png", "locomotion.png", "locomotive.png",
            "lollipop.png", "long drum.png", "lotion bottle.png", "lotus.png",
            "loudly crying face.png", "loudspeaker.png", "love-you gesture.png",
            "love letter.png", "low battery.png", "luggage.png", "lungs.png",
            "lying face.png", "macaw.png", "mage.png", "magic wand.png", "magnet.png",
            "magnifying glass tilted left.png", "magnifying glass tilted right.png",
            "mahjong red dragon.png", "male doctor.png", "male nurse.png", "male sign.png",
            "mammoth.png", "man's shoe.png", "man artist.png", "man astronaut.png",
            "man barista.png", "man biking.png", "man bouncing ball.png", "man bowing.png",
            "man cartwheeling.png", "man climbing.png", "man construction worker.png",
            "man cook.png", "man dancing.png", "man detective.png", "man elf.png",
            "man facepalming.png", "man factory worker.png", "man fairy.png",
            "man farmer.png", "man feeding baby.png", "man firefighter.png",
            "man frowning.png", "man genie.png", "man gesturing NO.png",
            "man gesturing OK.png", "man getting haircut.png", "man getting massage.png",
            "man golfing.png", "man guard.png", "man health worker.png",
            "man in lotus position.png", "man in manual wheelchair.png",
            "man in motorized wheelchair.png", "man in steamy room.png",
            "man in tuxedo.png", "man judge.png", "man juggling.png", "man kneeling.png",
            "man lifting weights.png", "man mage.png", "man mechanic.png",
            "man mountain biking.png", "man office worker.png", "man pilot.png",
            "man playing handball.png", "man playing water polo.png",
            "man police officer.png", "man pouting.png", "man raising hand.png",
            "man rowing boat.png", "man running.png", "man scientist.png",
            "man shrugging.png", "man singer.png", "man sneezing into elbow.png",
            "man standing.png", "man student.png", "man superhero.png",
            "man supervillain.png", "man surfing.png", "man swimming.png",
            "man teacher.png", "man technologist.png", "man tipping hand.png",
            "man vampire.png", "man walking.png", "man wearing turban.png",
            "man with medical mask.png", "man with veil.png", "man with white cane.png",
            "man zombie.png", "man, bald.png", "man, beard.png", "man, blond hair.png",
            "man, curly hair.png", "man, dark skin tone, bald.png",
            "man, dark skin tone, beard.png", "man, dark skin tone, blond hair.png",
            "man, dark skin tone, curly hair.png", "man, dark skin tone, red hair.png",
            "man, dark skin tone, white hair.png", "man, light skin tone, bald.png",
            "man, light skin tone, beard.png", "man, light skin tone, blond hair.png",
            "man, light skin tone, curly hair.png", "man, light skin tone, red hair.png",
            "man, light skin tone, white hair.png", "man, medium-dark skin tone, bald.png",
            "man, medium-dark skin tone, beard.png",
            "man, medium-dark skin tone, blond hair.png",
            "man, medium-dark skin tone, curly hair.png",
            "man, medium-dark skin tone, red hair.png",
            "man, medium-dark skin tone, white hair.png",
            "man, medium-light skin tone, bald.png",
            "man, medium-light skin tone, beard.png",
            "man, medium-light skin tone, blond hair.png",
            "man, medium-light skin tone, curly hair.png",
            "man, medium-light skin tone, red hair.png",
            "man, medium-light skin tone, white hair.png",
            "man, medium skin tone, bald.png", "man, medium skin tone, beard.png",
            "man, medium skin tone, blond hair.png",
            "man, medium skin tone, curly hair.png", "man, medium skin tone, red hair.png",
            "man, medium skin tone, white hair.png", "man, red hair.png",
            "man, white hair.png", "man.png", "mango.png", "mantelpiece clock.png",
            "manual wheelchair.png", "maple leaf.png", "maracas.png", "mark.png",
            "martial arts uniform.png", "mate.png", "maultasche.png",
            "mausoleum at halicarnassus.png", "meat consumption.png", "meat on bone.png",
            "mechanic.png", "mechanical arm.png", "mechanical leg.png",
            "medical gloves.png", "medical symbol.png", "medication.png", "megaphone.png",
            "melon.png", "melting face.png", "memo.png", "men's room.png",
            "men with bunny ears.png", "men wrestling.png", "mending heart.png",
            "mermaid.png", "merman.png", "merperson.png", "metro.png", "microbe.png",
            "microphone.png", "microscope.png", "middle finger.png", "military helmet.png",
            "military medal.png", "milk jug.png", "milky way.png", "minibus.png",
            "minus.png", "mirror ball.png", "mirror.png", "moai.png", "mobile info.png",
            "mobile message.png", "mobile phone off.png", "mobile phone with arrow.png",
            "mobile phone.png", "moka pot.png", "money-mouth face.png", "money bag.png",
            "money with wings.png", "monkey face.png", "monkey.png", "monorail.png",
            "moon cake.png", "moon viewing ceremony.png", "moose.png",
            "more information.png", "mosquito.png", "motor boat.png", "motor scooter.png",
            "motor.png", "motorbike helmet.png", "motorcycle.png",
            "motorized wheelchair.png", "motorway.png", "mount fuji.png",
            "mountain cableway.png", "mountain railway.png", "mountain.png",
            "mouse face.png", "mouse trap.png", "mouse.png", "mouth.png", "move.png",
            "movie camera.png", "Mrs Claus.png", "multiply.png", "mushroom.png",
            "musical keyboard.png", "musical note.png", "musical notes.png",
            "musical score.png", "muted speaker.png", "mx claus.png", "nail polish.png",
            "name badge.png", "narwhal.png", "national park.png", "nauseated face.png",
            "nazar amulet.png", "necktie.png", "nerd face.png", "nest with eggs.png",
            "nesting dolls.png", "neutral face.png", "NEW button.png", "new moon face.png",
            "new moon.png", "newspaper.png", "next track button.png", "NG button.png",
            "night with stars.png", "nine-thirty.png", "nine o'clock.png", "ninja.png",
            "no bicycles.png", "no entry.png", "no handshaking.png", "no littering.png",
            "no mobile phones.png", "no pedestrians.png", "no stencil.png",
            "non-potable water.png", "north.png", "nose.png",
            "notebook with decorative cover.png", "notebook.png",
            "nuclear power plant ruin.png", "nuclear power plant.png",
            "nuclear protection.png", "nuclear worker man.png", "nuclear worker woman.png",
            "nut and bolt.png", "O button (blood type).png", "octopus.png", "oden.png",
            "office building.png", "office worker.png", "ogre.png", "oil drum.png",
            "oil spill.png", "OK button.png", "OK hand.png", "ok stencil.png",
            "old key.png", "old man.png", "old woman.png", "older person.png", "olive.png",
            "om.png", "ON! arrow.png", "oncoming automobile.png", "oncoming bus.png",
            "oncoming fist.png", "oncoming police car.png", "oncoming taxi.png",
            "one-piece swimsuit.png", "one-thirty.png", "one o'clock.png", "onion.png",
            "open book.png", "open file folder.png", "open hands.png", "Ophiuchus.png",
            "optical disk.png", "orange book.png", "orange circle.png", "orange heart.png",
            "orange hexagon.png", "orange square.png", "orangutan.png", "orca.png",
            "otter.png", "outbox tray.png", "outlet.png", "overlapping black squares.png",
            "overlapping white and black squares.png", "overlapping white squares.png",
            "overview.png", "owl.png", "ox.png", "oyster.png", "P button.png",
            "package.png", "page facing up.png", "page move.png", "page with curl.png",
            "pager.png", "paintbrush.png", "palm down hand.png", "palm tree.png",
            "palm up hand.png", "palms up together.png", "pancakes.png", "panda.png",
            "paperclip.png", "parachute.png", "parking garage.png", "parrot.png",
            "part alternation mark.png", "party popper.png", "partying face.png",
            "passenger ship.png", "passport control.png", "patient clipboard.png",
            "patient file.png", "pause button.png", "paw prints.png", "pea pod.png",
            "peace symbol.png", "peacock.png", "peanuts.png", "pear.png", "pen.png",
            "pencil.png", "penguin.png", "pensive face.png", "people dialogue.png",
            "people hugging.png", "people with bunny ears.png", "people wrestling.png",
            "performing arts.png", "persevering face.png", "person biking.png",
            "person bouncing ball.png", "person bowing.png", "person cartwheeling.png",
            "person climbing.png", "person facepalming.png", "person feeding baby.png",
            "person fencing.png", "person frowning.png", "person gesturing NO.png",
            "person gesturing OK.png", "person getting haircut.png",
            "person getting massage.png", "person golfing.png", "person in bed.png",
            "person in lotus position.png", "person in manual wheelchair.png",
            "person in motorized wheelchair.png", "person in steamy room.png",
            "person in suit levitating.png", "person in tuxedo.png", "person juggling.png",
            "person kneeling.png", "person lifting weights.png",
            "person mountain biking.png", "person playing handball.png",
            "person playing water polo.png", "person pouting.png",
            "person raising hand.png", "person rowing boat.png", "person running.png",
            "person shrugging.png", "person sneezing into elbow.png", "person standing.png",
            "person surfing.png", "person swimming.png", "person taking bath.png",
            "person tipping hand.png", "person walking.png", "person wearing turban.png",
            "person with crown.png", "person with dog.png", "person with medical mask.png",
            "person with skullcap.png", "person with veil.png",
            "person with white cane.png", "person, bald.png", "person, beard.png",
            "person, blond hair.png", "person, curly hair.png",
            "person, dark skin tone, bald.png", "person, dark skin tone, beard.png",
            "person, dark skin tone, blond hair.png",
            "person, dark skin tone, curly hair.png",
            "person, dark skin tone, red hair.png",
            "person, dark skin tone, white hair.png", "person, light skin tone, bald.png",
            "person, light skin tone, beard.png", "person, light skin tone, blond hair.png",
            "person, light skin tone, curly hair.png",
            "person, light skin tone, red hair.png",
            "person, light skin tone, white hair.png",
            "person, medium-dark skin tone, bald.png",
            "person, medium-dark skin tone, beard.png",
            "person, medium-dark skin tone, blond hair.png",
            "person, medium-dark skin tone, curly hair.png",
            "person, medium-dark skin tone, red hair.png",
            "person, medium-dark skin tone, white hair.png",
            "person, medium-light skin tone, bald.png",
            "person, medium-light skin tone, beard.png",
            "person, medium-light skin tone, blond hair.png",
            "person, medium-light skin tone, curly hair.png",
            "person, medium-light skin tone, red hair.png",
            "person, medium-light skin tone, white hair.png",
            "person, medium skin tone, bald.png", "person, medium skin tone, beard.png",
            "person, medium skin tone, blond hair.png",
            "person, medium skin tone, curly hair.png",
            "person, medium skin tone, red hair.png",
            "person, medium skin tone, white hair.png", "person, red hair.png",
            "person, white hair.png", "person.png", "petri dish.png", "pick.png",
            "pickup truck.png", "picture.png", "pie.png", "pig face.png", "pig nose.png",
            "pig.png", "pigeon.png", "pile of poo.png", "pill.png", "pills.png",
            "pilot.png", "pinata.png", "pinched fingers.png", "pinching hand.png",
            "pine decoration.png", "pineapple.png", "ping pong.png", "pink heart.png",
            "Pisces.png", "pizza.png", "placard.png", "place of worship.png", "plaster.png",
            "plastic bottle.png", "play button.png", "play or pause button.png",
            "playground slide.png", "pleading face.png", "plunger.png", "plus.png",
            "polar bear.png", "polar explorer man.png", "polar explorer woman.png",
            "polar explorer.png", "polar research station.png", "police car light.png",
            "police car.png", "police officer.png", "pomegranate.png", "poodle.png",
            "pool 8 ball.png", "popcorn.png", "poppy.png", "porpoise.png",
            "post office.png", "postal horn.png", "postbox.png", "pot of food.png",
            "potable water.png", "potato.png", "potentiometer.png", "potted plant.png",
            "poultry leg.png", "pound banknote.png", "pouring liquid.png",
            "pouting cat.png", "power on-off symbol.png", "power on symbol.png",
            "power sleep symbol.png", "power symbol.png", "prayer beads.png", "pretzel.png",
            "prince.png", "princess.png", "printer.png", "prohibited.png",
            "purple circle.png", "purple heart.png", "purple hexagon.png",
            "purple square.png", "purse.png", "pushpin.png", "puzzle piece.png",
            "qr code.png", "quarantine.png", "rabbit face.png", "rabbit.png", "raccoon.png",
            "racing car.png", "radio button.png", "radio.png", "radioactive waste.png",
            "radioactive.png", "railway car.png", "railway track.png",
            "rainbow hexagon.png", "rainbow.png", "raised back of hand.png",
            "raised fist.png", "raised hand.png", "raising hands.png", "ram.png",
            "raspberry pi.png", "rat.png", "razor.png", "receipt.png", "record button.png",
            "recycling symbol.png", "red apple.png", "red circle.png", "red envelope.png",
            "red exclamation mark.png", "red eye.png", "red heart.png", "red hexagon.png",
            "red paper lantern.png", "red question mark.png", "red square.png",
            "red triangle pointed down.png", "red triangle pointed up.png",
            "regional indicator A.png", "regional indicator B.png",
            "regional indicator C.png", "regional indicator D.png",
            "regional indicator E.png", "regional indicator F.png",
            "regional indicator G.png", "regional indicator H.png",
            "regional indicator I.png", "regional indicator J.png",
            "regional indicator K.png", "regional indicator L.png",
            "regional indicator M.png", "regional indicator N.png",
            "regional indicator O.png", "regional indicator P.png",
            "regional indicator Q.png", "regional indicator R.png",
            "regional indicator S.png", "regional indicator T.png",
            "regional indicator U.png", "regional indicator V.png",
            "regional indicator W.png", "regional indicator X.png",
            "regional indicator Y.png", "regional indicator Z.png", "registered.png",
            "relieved face.png", "reminder ribbon.png", "repeat button.png",
            "repeat single button.png", "rescue worker's helmet.png", "restroom.png",
            "return.png", "reusable bag.png", "reverse button.png", "revolving hearts.png",
            "rhinoceros.png", "ribbon.png", "rice ball.png", "rice cracker.png",
            "right-facing fist.png", "right anger bubble.png",
            "right arrow curving down.png", "right arrow curving left.png",
            "right arrow curving up.png", "right arrow.png", "rightwards hand.png",
            "rightwards pushing hand.png", "ring buoy.png", "ring.png", "ringed planet.png",
            "roasted coffee bean.png", "roasted sweet potato.png", "robot.png", "rock.png",
            "rocket.png", "roll of paper.png", "rolled-up newspaper.png",
            "roller coaster.png", "roller skate.png", "rolling on the floor laughing.png",
            "rooster.png", "rose.png", "rosette.png", "round pushpin.png",
            "rounded symbol for cai.png", "rounded symbol for fu.png",
            "rounded symbol for lu.png", "rounded symbol for shou.png",
            "rounded symbol for shuangxi.png", "rounded symbol for xi.png",
            "rugby football.png", "running shirt.png", "running shoe.png",
            "sad but relieved face.png", "safety pin.png", "safety vest.png", "safety.png",
            "Sagittarius.png", "sailboat.png", "saline drip.png", "salt.png",
            "saluting face.png", "sandwich.png", "sanitizer spray.png", "Santa Claus.png",
            "sari.png", "satellite antenna.png", "satellite.png", "sauropod.png",
            "save.png", "saw.png", "saxophone.png", "scale.png", "scales.png", "scarf.png",
            "school.png", "schwabisch gmund forum gold und silber.png",
            "schwabisch gmund funfknopfturm.png", "schwabisch gmund ratshaus.png",
            "scientist.png", "scissors.png", "Scorpio.png", "scorpion.png",
            "screwdriver.png", "scroll horizontal.png", "scroll.png", "sea level rise.png",
            "seal.png", "seat.png", "see-no-evil monkey.png", "seedling.png", "selfie.png",
            "service dog.png", "service mark.png", "seven-thirty.png", "seven o'clock.png",
            "sewing needle.png", "shaking face.png", "shallow pan of food.png",
            "shamrock.png", "share.png", "shark.png", "shaved ice.png", "sheaf of rice.png",
            "shelter.png", "shield.png", "ship.png", "shooting star.png",
            "shopping bags.png", "shopping cart.png", "shortcake.png", "shorts.png",
            "shower.png", "shrimp.png", "shuffle tracks button.png", "shushing face.png",
            "sign of the horns.png", "signpost.png", "simple.png", "singer.png",
            "six-thirty.png", "six o'clock.png", "skateboard.png", "skier.png", "skis.png",
            "skull and crossbones.png", "skull.png", "skunk.png", "sled.png",
            "sleeping face.png", "sleepy face.png", "slightly frowning face.png",
            "slightly smiling face.png", "slot machine.png", "sloth.png",
            "small airplane.png", "small blue diamond.png", "small orange diamond.png",
            "smartwatch.png", "smiling cat with heart-eyes.png",
            "smiling face with halo.png", "smiling face with heart-eyes.png",
            "smiling face with hearts.png", "smiling face with horns.png",
            "smiling face with open hands.png", "smiling face with smiling eyes.png",
            "smiling face with sunglasses.png", "smiling face with tear.png",
            "smiling face.png", "smirking face.png", "snail.png", "snake.png",
            "sneezing face.png", "snow-capped mountain.png", "snowboarder.png",
            "snowflake.png", "snowman without snow.png", "snowman.png", "soap.png",
            "soccer ball.png", "social distancing.png", "socks.png", "soft ice cream.png",
            "softball.png", "solar cell.png", "solar energy.png", "SOON arrow.png",
            "sort.png", "SOS button.png", "sos stencil.png",
            "sound recording copyright.png", "space shuttle.png", "spade suit.png",
            "spade.png", "spaghetti.png", "sparkle.png", "sparkler.png", "sparkles.png",
            "sparkling heart.png", "spatzlepresse.png", "speak-no-evil monkey.png",
            "speaker high volume.png", "speaker low volume.png",
            "speaker medium volume.png", "speaking head.png", "speech balloon.png",
            "speedboat.png", "spider web.png", "spider.png", "spiral calendar.png",
            "spiral notepad.png", "spiral shell.png", "sponge.png", "spoon.png",
            "sport utility vehicle.png", "sports medal.png", "spouting-orca.png",
            "spouting whale.png", "square with left half black.png",
            "square with lower right diagonal black.png",
            "square with right half black.png", "square with upper left diagonal black.png",
            "squid.png", "squinting face with tongue.png", "stadium.png", "stairway.png",
            "star-struck.png", "star with left half black.png",
            "star with right half black.png", "star.png", "station.png",
            "statue of zeus at olympia.png", "steaming bowl.png", "stethoscope.png",
            "stick figure leaning left.png", "stick figure leaning right.png",
            "stick figure with arms raised.png",
            "stick figure with dress and arms raised.png",
            "stick figure with dress leaning left.png",
            "stick figure with dress leaning right.png", "stick figure with dress.png",
            "stick figure.png", "stomach.png", "stop button.png", "stop sign.png",
            "stopwatch.png", "straight ruler.png", "strawberry.png", "stretcher.png",
            "structural fire.png", "student.png", "studio microphone.png",
            "stuffed flatbread.png", "stuttgart fernsehturm.png", "sun behind cloud.png",
            "sun behind large cloud.png", "sun behind rain cloud.png",
            "sun behind small cloud.png", "sun with face.png", "sun.png", "sunflower.png",
            "sunglasses.png", "sunrise over mountains.png", "sunrise.png", "sunset.png",
            "superhero.png", "supervillain.png", "surveillance.png", "sushi.png",
            "suspension railway.png", "swab pliers.png", "swan.png", "sweat droplets.png",
            "swipe down.png", "swipe left.png", "swipe right.png", "swipe up.png",
            "swipe.png", "switch.png", "syringe.png", "T-Rex.png", "t-shirt.png",
            "tablet.png", "taco.png", "takeout box.png", "tamale.png", "tanabata tree.png",
            "tangerine.png", "tap.png", "tardis.png", "Taurus.png", "taxi.png",
            "teacher.png", "teacup without handle.png", "teapot.png",
            "tear-off calendar.png", "technologist.png", "teddy bear.png",
            "telephone receiver.png", "telephone.png", "telescope.png", "television.png",
            "temperature taking.png", "temple of artemis at ephesus.png", "ten-thirty.png",
            "ten o'clock.png", "tennis.png", "tent.png", "test tube.png", "thermometer.png",
            "thinking face.png", "thong sandal.png", "thought balloon.png", "thread.png",
            "three-thirty.png", "three finger operation.png", "three o'clock.png",
            "thumbs down.png", "thumbs up.png", "ticket.png", "tiger face.png", "tiger.png",
            "timer clock.png", "timer.png", "tired face.png", "toggle button state B.png",
            "toggle button.png", "toilet.png", "tomato.png", "tongue.png", "toolbox.png",
            "tooth.png", "toothbrush.png", "TOP arrow.png", "top hat.png", "tornado.png",
            "town.png", "trackball.png", "tractor.png", "trade mark.png", "train.png",
            "tram car.png", "tram.png", "transgender symbol.png", "transmission.png",
            "triangular ruler.png", "trident emblem.png", "troll.png", "trolleybus.png",
            "trophy.png", "tropical fish.png", "trowel.png", "trumpet.png", "tsunami.png",
            "tulip.png", "turkey.png", "turtle.png", "twelve-thirty.png",
            "twelve o'clock.png", "two-hump camel.png", "two-thirty.png", "two hearts.png",
            "two o'clock.png", "umbrella on ground.png", "umbrella with rain drops.png",
            "umbrella.png", "unamused face.png", "unicorn.png", "unlocked.png",
            "up-down arrow.png", "up-left arrow.png",
            "up-pointing triangle with left half black.png",
            "up-pointing triangle with right half black.png", "up-right arrow.png",
            "up arrow.png", "up down black arrow.png", "UP! button.png", "upload.png",
            "upside-down face.png", "upwards button.png", "vampire.png",
            "vertical traffic light.png", "vibration mode.png", "victory hand.png",
            "video camera.png", "video game.png", "videocassette.png",
            "viennese coffee house.png", "violin.png", "Virgo.png", "virtual reality.png",
            "volcano ashes.png", "volcano eruption.png", "volcano.png", "volleyball.png",
            "VS button.png", "vulcan salute.png", "waffle.png", "waning crescent moon.png",
            "waning gibbous moon.png", "warning fire.png", "warning strip right.png",
            "warning strip.png", "warning tsunami.png", "warning volcano.png",
            "warning.png", "wash hands.png", "washing machine.png", "wastebasket.png",
            "watch.png", "water buffalo.png", "water closet.png", "water pistol.png",
            "water wave.png", "watermelon.png", "waving hand.png", "wavy dash.png",
            "waxing crescent moon.png", "waxing gibbous moon.png", "weary cat.png",
            "weary face.png", "web syndication.png", "whale.png", "wheel chair.png",
            "wheel.png", "wheelbarrow.png", "wheelchair symbol.png", "white cane.png",
            "white circle.png", "white exclamation mark.png", "white flower.png",
            "white heart.png", "white hexagon.png", "white large square.png",
            "white medium-small square.png", "white medium square.png",
            "white pentagon.png", "white question mark.png", "white rectangle.png",
            "white small square.png", "white square button.png", "white square.png",
            "white vertical ellipse.png", "wifi.png", "wild fire.png", "wilted flower.png",
            "wind chime.png", "wind energy.png", "wind face.png", "window.png",
            "windsurfing.png", "wing.png", "winking face with tongue.png",
            "winking face.png", "wire.png", "wireframes.png", "wireless.png", "wolf.png",
            "woman's boot.png", "woman's clothes.png", "woman's hat.png",
            "woman's sandal.png", "woman artist.png", "woman astronaut.png",
            "woman barista.png", "woman biking.png", "woman bouncing ball.png",
            "woman bowing.png", "woman cartwheeling.png", "woman climbing.png",
            "woman construction worker.png", "woman cook.png", "woman dancing.png",
            "woman detective.png", "woman elf.png", "woman facepalming.png",
            "woman factory worker.png", "woman fairy.png", "woman farmer.png",
            "woman feeding baby.png", "woman firefighter.png", "woman frowning.png",
            "woman genie.png", "woman gesturing NO.png", "woman gesturing OK.png",
            "woman getting haircut.png", "woman getting massage.png", "woman golfing.png",
            "woman guard.png", "woman health worker.png", "woman in lotus position.png",
            "woman in manual wheelchair.png", "woman in motorized wheelchair.png",
            "woman in steamy room.png", "woman in tuxedo.png", "woman judge.png",
            "woman juggling.png", "woman kneeling.png", "woman lifting weights.png",
            "woman mage.png", "woman mechanic.png", "woman mountain biking.png",
            "woman office worker.png", "woman pilot.png", "woman playing handball.png",
            "woman playing water polo.png", "woman police officer.png", "woman pouting.png",
            "woman raising hand.png", "woman rowing boat.png", "woman running.png",
            "woman scientist.png", "woman shrugging.png", "woman singer.png",
            "woman sneezing into elbow.png", "woman standing.png", "woman student.png",
            "woman superhero.png", "woman supervillain.png", "woman surfing.png",
            "woman swimming.png", "woman teacher.png", "woman technologist.png",
            "woman tipping hand.png", "woman vampire.png", "woman walking.png",
            "woman wearing turban.png", "woman with headscarf.png",
            "woman with medical mask.png", "woman with veil.png",
            "woman with white cane.png", "woman zombie.png", "woman, bald.png",
            "woman, beard.png", "woman, blond hair.png", "woman, curly hair.png",
            "woman, dark skin tone, bald.png", "woman, dark skin tone, beard.png",
            "woman, dark skin tone, blond hair.png",
            "woman, dark skin tone, curly hair.png", "woman, dark skin tone, red hair.png",
            "woman, dark skin tone, white hair.png", "woman, light skin tone, bald.png",
            "woman, light skin tone, beard.png", "woman, light skin tone, blond hair.png",
            "woman, light skin tone, curly hair.png",
            "woman, light skin tone, red hair.png",
            "woman, light skin tone, white hair.png",
            "woman, medium-dark skin tone, bald.png",
            "woman, medium-dark skin tone, beard.png",
            "woman, medium-dark skin tone, blond hair.png",
            "woman, medium-dark skin tone, curly hair.png",
            "woman, medium-dark skin tone, red hair.png",
            "woman, medium-dark skin tone, white hair.png",
            "woman, medium-light skin tone, bald.png",
            "woman, medium-light skin tone, beard.png",
            "woman, medium-light skin tone, blond hair.png",
            "woman, medium-light skin tone, curly hair.png",
            "woman, medium-light skin tone, red hair.png",
            "woman, medium-light skin tone, white hair.png",
            "woman, medium skin tone, bald.png", "woman, medium skin tone, beard.png",
            "woman, medium skin tone, blond hair.png",
            "woman, medium skin tone, curly hair.png",
            "woman, medium skin tone, red hair.png",
            "woman, medium skin tone, white hair.png", "woman, red hair.png",
            "woman, white hair.png", "woman.png", "women's room.png",
            "women with bunny ears.png", "women wrestling.png", "wood.png",
            "woozy face.png", "world map.png", "worm.png", "worried face.png",
            "wrapped gift.png", "wrench.png", "writing hand.png", "x-ray.png", "yarn.png",
            "yawning face.png", "yellow circle.png", "yellow heart.png",
            "yellow hexagon.png", "yellow square.png", "yen banknote.png", "yo-yo.png",
            "zany face.png", "zebra.png", "zipper-mouth face.png", "zombie.png", "ZZZ.png"
    };

    /**
     * Creates an Iconizer and loads its TextureAtlas from the classpath. This must be called
     * during or after {@link ApplicationListener#create()} has been called by the framework.
     * This cannot be run on a headless backend because it renders to a FrameBuffer using a
     * shader, but it can be run on any other backend.
     * <br>
     * This uses a shader to colorize the selected icons using HSL.
     * <a href="https://gamedev.stackexchange.com/a/59808">Credit for most of the shader goes to Sam Hocevar</a>.
     */
    public Iconizer(){
//        ShaderProgram shader = new ShaderProgram(
//                "attribute vec4 a_position;\n" +
//                "attribute vec4 a_color;\n" +
//                "attribute vec2 a_texCoord0;\n" +
//                "uniform mat4 u_projTrans;\n" +
//                "varying vec4 v_color;\n" +
//                "varying vec2 v_texCoords;\n" +
//                "\n" +
//                "void main()\n" +
//                "{\n" +
//                "   v_color = a_color;\n" +
//                "   v_color.a = v_color.a * (255.0/254.0);\n" +
//                "   v_texCoords = a_texCoord0;\n" +
//                "   gl_Position =  u_projTrans * a_position;\n" +
//                "}\n",
//
//                "#ifdef GL_ES\n" +
//                "#define LOWP lowp\n" +
//                "precision mediump float;\n" +
//                "#else\n" +
//                "#define LOWP \n" +
//                "#endif\n" +
//                "varying vec2 v_texCoords;\n" +
//                "varying LOWP vec4 v_color;\n" +
//                "uniform sampler2D u_texture;\n" +
//                "const float eps = 1.0e-10;\n" +
//                "vec4 rgb2hsl(vec4 c)\n" +
//                "{\n" +
//                "    const vec4 J = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n" +
//                "    vec4 p = mix(vec4(c.bg, J.wz), vec4(c.gb, J.xy), step(c.b, c.g));\n" +
//                "    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n" +
//                "    float d = q.x - min(q.w, q.y);\n" +
//                "    float l = q.x * (1.0 - 0.5 * d / (q.x + eps));\n" +
//                "    return vec4(abs(q.z + (q.w - q.y) / (6.0 * d + eps)), (q.x - l) / (min(l, 1.0 - l) + eps), l, c.a);\n" +
//                "}\n" +
//                "\n" +
//                "vec4 hsl2rgb(vec4 c)\n" +
//                "{\n" +
//                "    const vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n" +
//                "    vec3 p = abs(fract(c.x + K.xyz) * 6.0 - K.www);\n" +
//                "    float v = (c.z + c.y * min(c.z, 1.0 - c.z));\n" +
//                "    return vec4(v * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), 2.0 * (1.0 - c.z / (v + eps))), c.w);\n" +
//                "}\n" +
//                "void main()\n" +
//                "{\n" +
//                "   vec4 tgt = texture2D( u_texture, v_texCoords );\n" +
//                "   vec4 hsl = rgb2hsl(tgt);\n" +
//                "   hsl.x = (v_color.x);\n" +
//                "   hsl.y = v_color.y;\n" +
//                "   hsl.z *= v_color.z;\n" +
//                "   gl_FragColor = hsl2rgb(hsl);\n" +
//                "}");
//        if(!shader.isCompiled()) throw new GdxRuntimeException(shader.getLog());
    }

    /**
     * Hashes the given seeds and calls {@link #generate(int, int, long)} with the hashed seed.
     * @param width the width in pixels of the Pixmap to produce
     * @param height the height in pixels of the Pixmap to produce
     * @param seeds will be hashed as a group using {@link #scrambleAll(Object...)}
     * @return a new Pixmap generated using the given size and seed
     */
    public Pixmap generate(int width, int height, Object... seeds){
        return generate(width, height, scrambleAll(seeds));
    }

    /**
     * Creates a new Pixmap with the given width and height, using the given long seed to randomly
     * select colors and halves of icons to draw.
     * @param width the width in pixels of the Pixmap to produce
     * @param height the height in pixels of the Pixmap to produce
     * @param seed a typically-unique long seed for random generation
     * @return a new Pixmap generated using the given size and seed
     */
    public Pixmap generate(int width, int height, long seed){
        seed = scramble(seed);
        int bgColor = hsl2rgb(
                (seed & 62) / 64f, // 1-5
                (seed >>> 6 & 15) / 64f + 0.7f, // 6-9
                (seed >>> 10 & 63) / 250f + 0.2f, // 10-15
                1f);
        // darker version of above
        int bgColor2 = hsl2rgb(
                (seed & 62) / 64f, // 1-5
                (seed >>> 6 & 15) / 64f + 0.7f, // 6-9
                (seed >>> 10 & 63) / 250f, // 10-15
                1f);

        long seed2 = scramble(seed);
        Pixmap l = new Pixmap(Gdx.files.classpath("icons/"+regions[confineLowerHalf(seed2, regions.length / 2)]));
        l.setFilter(Pixmap.Filter.BiLinear);
        Pixmap m = new Pixmap(Gdx.files.classpath("icons/"+regions[confineUpperHalf(seed2, regions.length / 2) + regions.length / 2]));
        m.setFilter(Pixmap.Filter.BiLinear);
        Pixmap o = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        o.setFilter(Pixmap.Filter.BiLinear);
        o.setColor(bgColor);
        o.fill();
        o.setColor(bgColor2);
        o.fillCircle(width / 2, height / 2, width / 4);

        hsl2rgb(l,
                ((seed >>> 47 & 62) + 1) / 64f, // 0-5 again and 48-53
                (seed >>> 17 & 15) / 100f + 0.85f, // 17-20
                (seed >>> 21 & 63) / 256f + 0.55f); // 21-26;
        hsl2rgb(m,
                (((seed >>> 47 & 62) + 1) + (seed >>> 26 & 6) - 3 & 63) / 64f, // 0-5 and 46-47 again and 27-28
                (seed >>> 29 & 15) / 150f + 0.85f - 0.035f + (seed >>> 33 & 7) / 100f, // 29-32 and 33-35
                (seed >>> 36 & 63) / 256f + 0.6f - 0.05f + (seed >>> 42 & 15) / 150f); // 36-41 and 42-45



        int full = l.getWidth();
        int hf = full / 2;

        o.drawPixmap(l, 0, 0, hf, full, 0, 0, width/2, height);
        o.drawPixmap(m, hf, 0, full, full, width/2, 0, width, height);

        return o;
    }

    /**
     * Converts the four HSLA components, each in the 0.0 to 1.0 range, to a packed float in RGBA format.
     * @param h hue, from 0.0 to 1.0
     * @param s saturation, from 0.0 to 1.0
     * @param l lightness, from 0.0 to 1.0
     * @param a alpha, from 0.0 to 1.0
     * @return an RGBA-format packed float
     */
    public static int hsl2rgb(final float h, final float s, final float l, final float a){
        float x = Math.min(Math.max(Math.abs(h * 6f - 3f) - 1f, 0f), 1f);
        float y = h + (2f / 3f);
        float z = h + (1f / 3f);
        y -= (int)y;
        z -= (int)z;
        y = Math.min(Math.max(Math.abs(y * 6f - 3f) - 1f, 0f), 1f);
        z = Math.min(Math.max(Math.abs(z * 6f - 3f) - 1f, 0f), 1f);
        float v = (l + s * Math.min(l, 1f - l));
        float d = 2f * (1f - l / (v + 1e-10f));
        v *= 255f;
        return (
                (int)(a * 127f) << 1
                        | (int)(v * MathUtils.lerp(1f, z, d)) << 8
                        | (int)(v * MathUtils.lerp(1f, y, d)) << 16
                        | (int)(v * MathUtils.lerp(1f, x, d)) << 24
        );
    }

    /**
     * Converts the four HSLA components, each in the 0.0 to 1.0 range, to RGB values, and uses those RGB values for
     * each pixel in {@code p}. This leaves alpha as-is.
     * @param p a Pixmap that will be edited in-place
     * @param h hue, from 0.0 to 1.0
     * @param s saturation, from 0.0 to 1.0
     * @param l lightness, from 0.0 to 1.0
     */
    public static void hsl2rgb(final Pixmap p, final float h, final float s, final float l){
        ByteBuffer buf = p.getPixels();
        float x = Math.min(Math.max(Math.abs(h * 6f - 3f) - 1f, 0f), 1f);
        float y = h + (2f / 3f);
        float z = h + (1f / 3f);
        y -= (int)y;
        z -= (int)z;
        y = Math.min(Math.max(Math.abs(y * 6f - 3f) - 1f, 0f), 1f);
        z = Math.min(Math.max(Math.abs(z * 6f - 3f) - 1f, 0f), 1f);
        float v = (l + s * Math.min(l, 1f - l));
        float d = 2f * (1f - l / (v + 1e-10f));
        v *= 255f;
        byte r = (byte)(v * MathUtils.lerp(1f, x, d));
        byte g = (byte)(v * MathUtils.lerp(1f, y, d));
        byte b = (byte)(v * MathUtils.lerp(1f, z, d));

        for (int i = 3, n = buf.limit(); i < n; i+=4) {
            buf.put(i-3, r);
            buf.put(i-2, g);
            buf.put(i-1, b);
        }
        buf.flip();
    }

    /**
     * Hashes {@code o} and scrambles the hash with {@link #scramble(long)}.
     * @param o any Object; will be hashed with {@link Objects#hashCode(Object)}.
     * @return the scrambled hash of o
     */
    public static long scramble(Object o) {
        return scramble(Objects.hashCode(o));
    }

    /**
     * Scrambled and hashes o1 and o2 with {@link #scramble(long)}. More thorough than
     * just calling (for example) {@link Objects#hash(Object...)} and scrambling that,
     * since Objects.hash() can only return about 4 billion results, and this can return,
     * in theory, over a billion times as many possible results.
     * @param o1 any Object to hash with {@link Objects#hashCode(Object)}.
     * @param o2 any Object to hash with {@link Objects#hashCode(Object)}.
     * @return the scrambled hash
     */
    public static long scramble(Object o1, Object o2) {
        return scramble(scramble(Objects.hashCode(o1)) + Objects.hashCode(o2));
    }

    /**
     * Scrambled and hashes every Object in {@code os}, using the previous hashes to
     * modify the input to the next hash, and so on. Very likely to be able to return
     * all 18 quintillion possible {@code long} hashes if given at least 3-4 objects.
     * @param os an array or varargs of Object items of any type, including null
     * @return the scrambled hash, or 0 if {@code os} is a null array
     */
    public static long scrambleAll(Object... os) {
        if(os == null) return 0L;
        long r = 0;
        for (int i = 0; i < os.length; i++) {
            r = scramble(Objects.hashCode(os[i]) + r);
        }
        return r;
    }
    /**
     * Given a long {@code x}, this randomly scrambles x, so it is (almost always) a very different long.
     * This can take any long and can return any long.
     * <br>
     * It is currently unknown if this has any fixed-points (inputs that produce an identical output), but
     * a step is taken at the start of the function to eliminate one major known fixed-point at 0.
     * <br>
     * This uses the MX3 unary hash by Jon Maiga, but XORs the input with 0xBBE0563303A4615FL before using MX3.
     * @param x any long, to be scrambled
     * @return a scrambled long derived from {@code x}
     */
    public static long scramble(long x) {
        x ^= 0xBBE0563303A4615FL;
        x ^= x >>> 32;
        x *= 0xBEA225F9EB34556DL;
        x ^= x >>> 29;
        x *= 0xBEA225F9EB34556DL;
        x ^= x >>> 32;
        x *= 0xBEA225F9EB34556DL;
        return x ^ x >>> 29;
    }


    /**
     * Given a long {@code x} and an int {@code bound}, this randomly scrambles the low bits of x, so it produces an int
     * between 0 (inclusive) and bound (exclusive). The bound is permitted to be negative; it is still exclusive then.
     * This uses the low bits only so that you can get a second bounded int with {@link #confineUpperHalf(long, int)}.
     * <br>
     * This does not randomize {@code x}; use {@link #scramble} methods to do that first.
     * @param x any long; should be scrambled with {@link #scramble}
     * @param bound the exclusive outer bound
     * @return a scrambled int between 0 (inclusive) and {@code bound} (exclusive) derived from {@code x}
     */
    public static int confineLowerHalf(long x, int bound) {
        return (bound = (int) ((bound * (x & 0xFFFFFFFFL)) >> 32)) + (bound >>> 31);
    }

    /**
     * Given a long {@code x} and an int {@code bound}, this randomly scrambles the low bits of x, so it produces an int
     * between 0 (inclusive) and bound (exclusive). The bound is permitted to be negative; it is still exclusive then.
     * This uses the high bits only so that you can get a second bounded int with {@link #confineLowerHalf(long, int)}.
     * <br>
     * This does not randomize {@code x}; use {@link #scramble} methods to do that first.
     * @param x any long; should be scrambled with {@link #scramble}
     * @param bound the exclusive outer bound
     * @return a scrambled int between 0 (inclusive) and {@code bound} (exclusive) derived from {@code x}
     */
    public static int confineUpperHalf(long x, int bound) {
        return (bound = (int) ((bound * (x >>> 32)) >> 32)) + (bound >>> 31);
    }
}
