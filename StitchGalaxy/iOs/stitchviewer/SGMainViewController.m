//
//  SGMainViewController.m
//  stitchviewer
//
//  Created by 123 on 06.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "SGMainViewController.h"

@interface SGMainViewController ()

@end

@implementation SGMainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void) segmentAction: (UISegmentedControl *) segmentedControl
{
    // Update the label with the segment number
    NSString *segmentNumber = [NSString stringWithFormat:@"%0d",
                               segmentedControl.selectedSegmentIndex + 1];
    [(UITextView *)self.view setText:segmentNumber];
}
- (void) loadView
{
    [super loadView];
    
    // Create the segmented control
    NSArray *buttonNames = [NSArray arrayWithObjects:
                            @"One", @"Two", @"Three", @"Four", @"Five", @"Six", nil];
    UISegmentedControl* segmentedControl = [[UISegmentedControl alloc]
                                            initWithItems:buttonNames];
    segmentedControl.segmentedControlStyle = UISegmentedControlStyleBar;
    segmentedControl.momentary = YES;
    [segmentedControl addTarget:self action:@selector(segmentAction:)
               forControlEvents:UIControlEventValueChanged];
    
    // Add it to the navigation bar
    self.navigationItem.titleView = segmentedControl;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
