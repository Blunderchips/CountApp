@echo off

title generate_title_images

convert "title (1920 x 1080).jpg" -resize 1024x500! -quality 100 "title (1024 x 500).jpg"
convert "title (1920 x 1080).jpg" -resize 180x120! -quality 100 "title (180 x 120).jpg"
convert "title (1920 x 1080).jpg" -resize 1280x720! -quality 100 "title (1280 x 720).jpg"
