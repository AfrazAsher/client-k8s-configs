#!/bin/bash

# Function to check if a file contains only the specified two lines
check_file() {
    if grep -q "apiVersion: kustomize.config.k8s.io/v1beta1" "$1" && \
       grep -q "kind: Kustomization" "$1" && \
       ! grep -qEv "apiVersion: kustomize.config.k8s.io/v1beta1|kind: Kustomization" "$1"; then
        echo "$1"
    fi
}

# Function to traverse directories and search for files
traverse_directory() {
    local dir="$1"
    for entry in "$dir"/*; do
        if [ -d "$entry" ]; then
            if [ "$(basename "$entry")" = ".git" ]; then
                continue  # Skip .git directories
            fi
            traverse_directory "$entry"
        elif [ -f "$entry" ]; then
            check_file "$entry"
        fi
    done
}

# Starting point for the search
start_directory="/c/Users/I334714/Desktop/DC25/gardener-k8s-argocd-apps/app-prerequisites/overlays/dc25/"  # Change this to your desired directory

# Check if the starting directory exists
if [ -d "$start_directory" ]; then
    traverse_directory "$start_directory"
else
    echo "Directory $start_directory not found."
fi

