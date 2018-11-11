//
//  ViewController.swift
//  HygieaWasteSortingAppIOS
//  A lot of the code is based on many tutorials
//  Created by Hesham Alghamdi on 9/1/18.
//  Copyright © 2018 Arizona State University. All rights reserved.
//
import Foundation
import UIKit
import AVFoundation

class ViewController: UIViewController {
    
    let loginController = LoginViewController()
    var pressedBtnString: String = String()
    var captureSession = AVCaptureSession()
    var backCamera : AVCaptureDevice?
    var frontCamera : AVCaptureDevice?
    var currentCamera : AVCaptureDevice?
    
    var photoOutput: AVCapturePhotoOutput?
    
    var previewLayer: AVCaptureVideoPreviewLayer?
    
    var  image: UIImage?
    var persImageData: Data?
    
    @IBOutlet weak var recycleButton: UIButton!
    @IBOutlet weak var compostButton: UIButton!
    @IBOutlet weak var captureButton: UIButton!
    @IBOutlet weak var compostLabel: UILabel!
    @IBOutlet weak var captureLabel: UILabel!
    @IBOutlet weak var recycleLabel: UILabel!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        compostLabel.textColor = UIColor.white
        captureLabel.textColor = UIColor.white
        recycleLabel.textColor = UIColor.white
        
        if(!loginController.isDev){
            recycleButton.isHidden = true
            recycleButton.isEnabled = false
            compostButton.isHidden = true
            compostButton.isEnabled = false
            recycleLabel.isHidden = true
            recycleLabel.isEnabled = false
            compostLabel.isHidden = true
            compostLabel.isEnabled = false
        }
        setupCaptureSession()
        setupDevice()
        setupIO()
        setupPreview()
        startRunningCaptureSession()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    
    func setupCaptureSession(){
        captureSession.sessionPreset = AVCaptureSession.Preset.photo
        
    }
    
    func setupDevice(){
        let deviceDiscoverySession = AVCaptureDevice.DiscoverySession(deviceTypes: [AVCaptureDevice.DeviceType.builtInWideAngleCamera], mediaType: AVMediaType.video, position: AVCaptureDevice.Position.unspecified)
        
        let devices = deviceDiscoverySession.devices
        
        for device in devices {
            if device.position == AVCaptureDevice.Position.back{
                backCamera = device
            }else if device.position == AVCaptureDevice.Position.front {
                frontCamera = device
            }
        }
        currentCamera = backCamera
        
    }
    
    
    func setupIO(){
        do{
            let captureDeviceInput = try AVCaptureDeviceInput(device: currentCamera! )
            captureSession.addInput(captureDeviceInput)
            photoOutput = AVCapturePhotoOutput()
            photoOutput?.setPreparedPhotoSettingsArray([AVCapturePhotoSettings(format:[AVVideoCodecKey: AVVideoCodecType.jpeg])], completionHandler: nil)
            captureSession.addOutput(photoOutput!)
        }
        catch{
            print(error)
        }
        
    }
    
    func setupPreview(){
        previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer?.videoGravity = AVLayerVideoGravity.resizeAspectFill
        previewLayer?.connection?.videoOrientation = AVCaptureVideoOrientation.portrait
        previewLayer?.frame = self.view.frame
        self.view.layer.insertSublayer(previewLayer!, at: 0)
        
    }
    
    func startRunningCaptureSession(){
        captureSession.startRunning()
    }
    
    
    
    @IBAction func CameraButtonAction(_ sender: Any) {
        pressedBtnString = "/unlabled/"
        let settings = AVCapturePhotoSettings()
        photoOutput?.capturePhoto(with: settings, delegate: self)
    }
    @IBAction func RecycleButtonAction(_ sender: Any) {
        pressedBtnString = "/recycle/"
        let settings = AVCapturePhotoSettings()
        photoOutput?.capturePhoto(with: settings, delegate: self)
    }
    @IBAction func CompostButtonAction(_ sender: Any) {
        pressedBtnString = "/compost/"
        let settings = AVCapturePhotoSettings()
        photoOutput?.capturePhoto(with: settings, delegate: self)
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
                if segue.identifier == "LoadingSegue" {
                    let loadingVC = segue.destination as! ActIndicatorViewController
                    loadingVC.data = self.persImageData!
                }
    }
    
    
    //    override func didReceiveMemoryWarning() {
    //        super.didReceiveMemoryWarning()
    //        // Dispose of any resources that can be recreated.
    //    }
    
    
}

extension ViewController: AVCapturePhotoCaptureDelegate{
    func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        if let imageData = photo.fileDataRepresentation(){
            persImageData = imageData
            image = UIImage(data: imageData)
            performSegue(withIdentifier: "LoadingSegue" , sender: nil)
            
        }
    }
}
