# Use nginx to serve static files
FROM nginx:alpine

# Copy your dist folder to nginx html folder
COPY dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Run nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]
